package com.vistajet.vistajet.handler;

import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionResponse> build(
            BusinessErrorCodes errorCode,
            String message,
            Set<String> validationSet,
            Map<String, String> errorMap
    ) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        ExceptionResponse.builder()
                                .code(errorCode.getCode())
                                .businessErrorDescription(errorCode.getDescription())
                                .error(message)
                                .validationError(validationSet)
                                .errors(errorMap)
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException ex) {
        return build(
                BusinessErrorCodes.BAD_CREDENTIALS,
                "Invalid username or password",
                null,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return build(
                BusinessErrorCodes.VALIDATION_FAILED,
                "Validation failed",
                null,
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage())
        );

        return build(
                BusinessErrorCodes.VALIDATION_FAILED,
                "Validation failed",
                null,
                errors
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionResponse.builder()
                        .code(404)
                        .error(ex.getMessage())
                        .businessErrorDescription("Resource not found")
                        .build()
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequest(InvalidRequestException ex) {

        return build(
                BusinessErrorCodes.BAD_REQUEST,
                ex.getMessage(),
                null,
                null
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntime(RuntimeException ex) {

        return build(
                BusinessErrorCodes.BAD_REQUEST,
                ex.getMessage(),
                null,
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneral(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .code(500)
                                .error("Server error. Please contact admin")
                                .businessErrorDescription("Internal Server Error")
                                .build()
                );
    }
}

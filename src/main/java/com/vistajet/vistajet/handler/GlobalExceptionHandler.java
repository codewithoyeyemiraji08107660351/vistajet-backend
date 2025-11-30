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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(BadCredentialsException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .code(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                                .error(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                                .build()

                );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(MethodArgumentNotValidException exp){

        Set<String> errors = new HashSet<>();
        exp.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    var errorMessage =  error.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ExceptionResponse.builder()
                                .validationError(errors)
                                .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .code(BusinessErrorCodes.VALIDATION_FAILED.getCode())
                        .error("Validation failed")
                        .errors(errors)
                        .build());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(RuntimeException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .code(BusinessErrorCodes.BAD_REQUEST.getCode())
                                .error(BusinessErrorCodes.BAD_REQUEST.getDescription())
                                .build()
                );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalid(InvalidRequestException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception exp){
        exp.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .code(500)
                                .error("Server is breakdown, pls contact the administrator")
                                .build()
                );
    }

    private ResponseEntity<?> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }



}

package com.vistajet.vistajet.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {

    BAD_CREDENTIALS(301, HttpStatus.BAD_REQUEST, "Incorrect Username / or password"),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Email Already Exists"),
    VALIDATION_FAILED(1001, HttpStatus.BAD_REQUEST, "Validation failed"),

    ;

    @Getter
    private final int  code;
    @Getter
    private final HttpStatus status;
    @Getter
    private final String description;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.status = status;
        this.description = description;
    }
}

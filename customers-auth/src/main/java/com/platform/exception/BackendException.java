package com.platform.exception;

import org.springframework.http.HttpStatus;

public class BackendException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public BackendException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BackendException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message,cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}



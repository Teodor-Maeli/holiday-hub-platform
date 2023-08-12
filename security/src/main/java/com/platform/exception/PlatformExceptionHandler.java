package com.platform.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class PlatformExceptionHandler {

    private static final String MESSAGE = "message";
    private static final String STATUS = "status";

    @ExceptionHandler(BackendException.class)
    private ResponseEntity<Map<String, Object>> handleException(BackendException be) {
        return ResponseEntity
            .status(be.getHttpStatus())
            .body(Map.of(
                MESSAGE, be.getMessage(),
                STATUS, be.getHttpStatus()
            ));
    }
}

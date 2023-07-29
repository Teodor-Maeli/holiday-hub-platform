package com.platform.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlatformExceptionHandler {

    private static final String MESSAGE = "message";
    private static final String ACTION = "action";
    private static final String STATUS = "status";

    @ExceptionHandler(SecurityException.class)
    private ResponseEntity<Map<String, Object>> handleException(SecurityException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(MESSAGE, e.getMessage());
        response.put(ACTION, e.getAction());
        response.put(STATUS, e.getHttpStatus());

        return ResponseEntity.status(e.getHttpStatus())
                             .body(response);
    }
}

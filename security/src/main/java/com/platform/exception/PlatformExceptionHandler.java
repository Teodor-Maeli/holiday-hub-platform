package com.platform.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PlatformExceptionHandler {

    private static final String MESSAGE = "message";
    private static final String STATUS = "status";

    @ExceptionHandler(SecurityException.class)
    private ResponseEntity<Map<String, Object>> handleException(SecurityException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(MESSAGE, e.getMessage());
        response.put(STATUS, e.getHttpStatus());

        log.error("Exception occurred: ", e);
        return ResponseEntity.status(e.getHttpStatus())
                             .body(response);
    }
}

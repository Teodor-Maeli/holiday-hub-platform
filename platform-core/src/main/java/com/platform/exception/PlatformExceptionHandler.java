package com.platform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class PlatformExceptionHandler {

  private static final String MESSAGE = "message";

  private static final String STATUS = "status";

  private static final String DETAILS = "details";

  private static final Logger LOGGER = LoggerFactory.getLogger(PlatformExceptionHandler.class);

  @ExceptionHandler(PlatformBackendException.class)
  private ResponseEntity<Map<String, Object>> handleException(PlatformBackendException be) {
    LOGGER.error("Exception has occurred: ", be);
    return ResponseEntity
        .status(be.getHttpStatus())
        .body(Map.of(
            MESSAGE, be.getMessage(),
            STATUS, be.getHttpStatus(),
            DETAILS, be.getDetails()
        ));
  }
}

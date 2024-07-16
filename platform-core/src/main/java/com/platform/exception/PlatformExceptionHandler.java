package com.platform.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class PlatformExceptionHandler {

  @ExceptionHandler(PlatformBackendException.class)
  private ResponseEntity<Map<String, Object>> handleException(PlatformBackendException be) {
    Map<String, Object> erroneousResponse = new HashMap<>();
    erroneousResponse.put("message", be.getMessage());
    erroneousResponse.put("status", be.getHttpStatus());
    erroneousResponse.put("details", be.getDetails());
    erroneousResponse.put("exception", be.getClass());

    if (be.getCause() != null) {
      erroneousResponse.put("cause", be.getCause().getClass());
    }

    log.error("Exception has occurred: ", be);
    return ResponseEntity
        .status(be.getHttpStatus())
        .body(erroneousResponse);
  }
}

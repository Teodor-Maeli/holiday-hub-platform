package com.platform.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class PlatformExceptionHandler {

  @ExceptionHandler(PlatformBackendException.class)
  private ResponseEntity<Map<String, Object>> handleException(PlatformBackendException be) {
    Map<String, Object> erroneousResponse = new HashMap<>();
    erroneousResponse.put("message", be.getMessage());
    erroneousResponse.put("status", be.getHttpStatus());
    erroneousResponse.put("details", be.getDetails());

    erroneousResponse =
        erroneousResponse.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != null || entry.getValue() != null)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));

    log.error("Exception has occurred: ", be);
    return ResponseEntity
        .status(be.getHttpStatus())
        .body(erroneousResponse);
  }
}

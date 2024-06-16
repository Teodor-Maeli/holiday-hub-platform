package com.platform.exception;

import com.platform.util.ObjectUtil;
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

  private static final String MESSAGE = "message";
  private static final String STATUS = "status";
  private static final String DETAILS = "details";


  @ExceptionHandler(PlatformBackendException.class)
  private ResponseEntity<Map<String, Object>> handleException(PlatformBackendException be) {
    Map<String, Object> erroneousResponse = new HashMap<>();
    erroneousResponse.put(MESSAGE, be.getMessage());
    erroneousResponse.put(STATUS, be.getHttpStatus());
    erroneousResponse.put(DETAILS, be.getDetails());

    erroneousResponse =
        erroneousResponse.entrySet()
            .stream()
            .filter(entry -> ObjectUtil.noneNull(entry.getKey(), entry.getValue()))
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

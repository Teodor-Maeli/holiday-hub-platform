package com.platform.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Accessors(chain = true)
public class PlatformBackendException extends RuntimeException {

  private HttpStatus httpStatus;
  private String message;
  private String details;
  private Throwable cause;
}



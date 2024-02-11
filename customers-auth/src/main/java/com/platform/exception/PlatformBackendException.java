package com.platform.exception;

import org.springframework.http.HttpStatus;

public class PlatformBackendException extends RuntimeException {

  private final String message;

  private final HttpStatus httpStatus;

  public PlatformBackendException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public PlatformBackendException(String message, HttpStatus httpStatus, Throwable cause) {
    super(cause);
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



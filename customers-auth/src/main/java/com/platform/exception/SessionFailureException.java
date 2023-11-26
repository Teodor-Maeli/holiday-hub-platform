package com.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class SessionFailureException extends AuthenticationException {

  private final HttpStatus httpStatus;
  private String redirectUrl;

  public SessionFailureException(Exception e, HttpStatus httpStatus) {
    super(e.getMessage(), e.getCause());
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}

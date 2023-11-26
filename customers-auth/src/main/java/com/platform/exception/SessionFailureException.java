package com.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class SessionFailureException extends AuthenticationException {

  private HttpStatus httpStatus;
  private String redirectUrl;

  public SessionFailureException(String msg, Throwable cause, HttpStatus httpStatus) {
    super(msg, cause);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}

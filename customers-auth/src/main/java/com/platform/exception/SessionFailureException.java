package com.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class SessionFailureException extends AuthenticationException {

  private final HttpStatus httpStatus;
  private final String redirectUrl;

  public SessionFailureException(Exception e, HttpStatus httpStatus, String redirectUrl) {
    super(e.getMessage(), e.getCause());
    this.httpStatus = httpStatus;
    this.redirectUrl = redirectUrl;
  }

  public SessionFailureException(Exception e, HttpStatus httpStatus) {
    super(e.getMessage(), e.getCause());
    this.httpStatus = httpStatus;
    this.redirectUrl = null;
  }


  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }
}

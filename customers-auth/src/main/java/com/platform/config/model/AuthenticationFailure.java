package com.platform.config.model;

public class AuthenticationFailure {

  private String timestamp;

  private String message;

  private AuthenticationFailure() {
  }

  public static AuthenticationFailure create() {
    return new AuthenticationFailure();
  }

  public AuthenticationFailure withTimestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public AuthenticationFailure withMessage(String message) {
    this.message = message;
    return this;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }
}

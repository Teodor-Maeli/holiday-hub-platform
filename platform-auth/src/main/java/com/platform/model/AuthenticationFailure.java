package com.platform.model;

public class AuthenticationFailure {

  private String timestamp;

  private String message;

  private String details;

  private AuthenticationFailure() {
  }

  public static AuthenticationFailure create() {
    return new AuthenticationFailure();
  }

  public AuthenticationFailure withDetails(String details) {
    this.details = details;
    return this;
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

  public String getDetails() {
    return details;
  }
}

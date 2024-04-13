package com.platform.model;

/**
 * Base request class.
 * Since 1.0
 */

public class RegistrationRequest {

  private String username;

  private String password;

  private String emailAddress;

  private String phoneNumber;

  private String redirectUrl;

  private String returnUrl;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public RegistrationRequest setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
    return this;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public RegistrationRequest setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
    return this;
  }
}

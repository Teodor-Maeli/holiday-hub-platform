package com.platform.rest.resource;

import java.time.LocalDateTime;

/**
 * Base request class.
 * Since 1.0
 */

public class RegistrationRequest {

  private String username;

  private String password;

  private String emailAddress;

  private String phoneNumber;

  private LocalDateTime subscriptionStarts;

  private LocalDateTime subscriptionEnds;

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

  public LocalDateTime getSubscriptionStarts() {
    return subscriptionStarts;
  }

  public void setSubscriptionStarts(LocalDateTime subscriptionStarts) {
    this.subscriptionStarts = subscriptionStarts;
  }

  public LocalDateTime getSubscriptionEnds() {
    return subscriptionEnds;
  }

  public void setSubscriptionEnds(LocalDateTime subscriptionEnds) {
    this.subscriptionEnds = subscriptionEnds;
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
}

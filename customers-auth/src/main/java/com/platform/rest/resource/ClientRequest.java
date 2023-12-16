package com.platform.rest.resource;

import java.time.LocalDateTime;

/**
 * 27.05.2023.
 *
 * <p>Base request class.</p>
 * <p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */

public class ClientRequest {

  private String username;

  private String password;

  private String emailAddress;

  private String phoneNumber;

  private Boolean premium;

  private Boolean enabled;

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

  public Boolean getPremium() {
    return premium;
  }

  public void setPremium(Boolean premium) {
    this.premium = premium;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
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

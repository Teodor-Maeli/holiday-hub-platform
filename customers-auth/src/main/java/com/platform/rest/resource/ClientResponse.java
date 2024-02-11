package com.platform.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.platform.domain.entity.AuthTokenAuditInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 27.05.2023.
 *
 * <p>Base response class.</p>
 * <p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */

@JsonInclude(Include.NON_NULL)
public class ClientResponse {

  private Long id;

  private String username;

  private String emailAddress;

  private String phoneNumber;

  private Boolean enabled;

  private Boolean premium;

  private SubscriptionResponse subscription;

  private List<AuthTokenAuditInfo> authTokensAuditInfo;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime registeredDate;

  private Set<Role> roles;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getRegisteredDate() {
    return registeredDate;
  }

  public void setRegisteredDate(LocalDateTime registeredDate) {
    this.registeredDate = registeredDate;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getPremium() {
    return premium;
  }

  public void setPremium(Boolean premium) {
    this.premium = premium;
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

  public SubscriptionResponse getSubscription() {
    return subscription;
  }

  public void setSubscription(SubscriptionResponse subscription) {
    this.subscription = subscription;
  }

  public List<AuthTokenAuditInfo> getAuthTokensAuditInfo() {
    return authTokensAuditInfo;
  }

  public void setAuthTokensAuditInfo(List<AuthTokenAuditInfo> authTokensAuditInfo) {
    this.authTokensAuditInfo = authTokensAuditInfo;
  }
}

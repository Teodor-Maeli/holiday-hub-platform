package com.platform.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.platform.common.model.Role;
import com.platform.domain.entity.AuthenticationAuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Base response class.
 * Since 1.0
 */

@JsonInclude(Include.NON_NULL)
public class ClientResponse {

  private Long id;

  private String username;

  private String emailAddress;

  private String phoneNumber;

  private List<SubscriptionResponse> subscriptions;

  private List<AuthenticationAuditLog> authenticationAuditLogs;

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

  public List<SubscriptionResponse> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(List<SubscriptionResponse> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public List<AuthenticationAuditLog> getAuthenticationAuditLogs() {
    return authenticationAuditLogs;
  }

  public void setAuthenticationAuditLogs(List<AuthenticationAuditLog> authenticationAuditLogs) {
    this.authenticationAuditLogs = authenticationAuditLogs;
  }
}

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.platform.persistence.entity.AuthenticationLogEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Base response class.
 * Since 1.0
 */

@JsonInclude(Include.NON_NULL)
public class Client {

  private Long id;

  private String username;

  private String emailAddress;

  private String phoneNumber;

  private List<Subscription> subscriptions;

  private List<AuthenticationLogEntity> authenticationLogs;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime registeredDate;

  private Set<ConsumerAuthority> authorities;


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

  public Set<ConsumerAuthority> getRoles() {
    return authorities;
  }

  public void setRoles(Set<ConsumerAuthority> authorities) {
    this.authorities = authorities;
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

  public List<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(List<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public List<AuthenticationLogEntity> getAuthenticationLogs() {
    return authenticationLogs;
  }

  public void setAuthenticationLogs(List<AuthenticationLogEntity> authenticationLogs) {
    this.authenticationLogs = authenticationLogs;
  }
}

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationLog {

  private Long id;
  private Client client;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private AuthenticationStatus authenticationStatus;
  private AuthenticationStatusReason statusReason;
  private String message;
  private Boolean statusResolved;

  public Long getId() {
    return id;
  }

  public AuthenticationLog setId(Long id) {
    this.id = id;
    return this;
  }

  public Client getClient() {
    return client;
  }

  public AuthenticationLog setClient(Client client) {
    this.client = client;
    return this;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public AuthenticationLog setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }

  public AuthenticationLog setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
    return this;
  }

  public AuthenticationStatus getAuthenticationStatus() {
    return authenticationStatus;
  }

  public AuthenticationLog setAuthenticationStatus(AuthenticationStatus authenticationStatus) {
    this.authenticationStatus = authenticationStatus;
    return this;
  }

  public AuthenticationStatusReason getStatusReason() {
    return statusReason;
  }

  public AuthenticationLog setStatusReason(AuthenticationStatusReason statusReason) {
    this.statusReason = statusReason;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public AuthenticationLog setMessage(String message) {
    this.message = message;
    return this;
  }

  public Boolean getStatusResolved() {
    return statusResolved;
  }

  public AuthenticationLog setStatusResolved(Boolean statusResolved) {
    this.statusResolved = statusResolved;
    return this;
  }
}

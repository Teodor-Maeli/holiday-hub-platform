package com.platform;

import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;

import java.util.Objects;

public class AuthenticationLogFacts {

  private ClientUserDetails clientDetails;

  private AuthenticationStatus status;

  private AuthenticationStatusReason reason;

  private Boolean resolved;

  private AuthenticationLogFacts() {

  }

  public static AuthenticationLogFacts initialize() {
    return new AuthenticationLogFacts();
  }

  private void validate() {
    Objects.requireNonNull(clientDetails, "clientDetails cannot be null!");
    Objects.requireNonNull(reason, "reason cannot be null!");
    Objects.requireNonNull(status, "status cannot be null!");
    Objects.requireNonNull(resolved, "resolved cannot be null!");
  }

  public AuthenticationLogEntity toEntity() {
    AuthenticationLogEntity authenticationLog = new AuthenticationLogEntity();
    authenticationLog.setAuthenticationStatus(status);
    authenticationLog.setStatusReason(reason);
    authenticationLog.setStatusResolved(resolved);
    authenticationLog.setClient(clientDetails.client());

    return authenticationLog;
  }

  public AuthenticationStatusReason getReason() {
    return reason;
  }

  public AuthenticationLogFacts withReason(AuthenticationStatusReason reason) {
    this.reason = reason;
    return this;
  }

  public AuthenticationStatus getStatus() {
    return status;
  }

  public AuthenticationLogFacts withStatus(AuthenticationStatus status) {
    this.status = status;
    return this;
  }

  public Boolean getResolved() {
    return resolved;
  }

  public AuthenticationLogFacts withStatusResolved(Boolean resolved) {
    this.resolved = resolved;
    return this;
  }

  public ClientUserDetails getClientDetails() {
    return clientDetails;
  }

  public AuthenticationLogFacts withClientDetails(ClientUserDetails clientDetails) {
    this.clientDetails = clientDetails;
    return this;
  }
}

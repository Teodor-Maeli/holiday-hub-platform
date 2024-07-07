package com.platform;

import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.CustomerUserDetails;
import com.platform.persistence.entity.AuthenticationAttempt;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationDetails {

  private CustomerUserDetails clientDetails;
  private AuthenticationStatus status;
  private AuthenticationStatusReason reason;
  private Boolean resolved;

  public static AuthenticationDetails create() {
    return new AuthenticationDetails();
  }

  private void validate() {
    Objects.requireNonNull(clientDetails, "clientDetails cannot be null!");
    Objects.requireNonNull(reason, "reason cannot be null!");
    Objects.requireNonNull(status, "status cannot be null!");
    Objects.requireNonNull(resolved, "resolved cannot be null!");
  }

  public AuthenticationAttempt toEntity() {
    validate();

    AuthenticationAttempt authenticationLog = new AuthenticationAttempt();
    authenticationLog.setAuthenticationStatus(status);
    authenticationLog.setStatusReason(reason);
    authenticationLog.setStatusResolved(resolved);
    authenticationLog.setCustomer(clientDetails.client());

    return authenticationLog;
  }
}

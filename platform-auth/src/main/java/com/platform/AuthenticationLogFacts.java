package com.platform;

import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
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
public class AuthenticationLogFacts {

  private ClientUserDetails clientDetails;
  private AuthenticationStatus status;
  private AuthenticationStatusReason reason;
  private Boolean resolved;

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
    validate();

    AuthenticationLogEntity authenticationLog = new AuthenticationLogEntity();
    authenticationLog.setAuthenticationStatus(status);
    authenticationLog.setStatusReason(reason);
    authenticationLog.setStatusResolved(resolved);
    authenticationLog.setClient(clientDetails.client());

    return authenticationLog;
  }
}

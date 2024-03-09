package com.platform.aspect;

import com.platform.config.StatelessAuthenticationFailureHandler;
import com.platform.config.StatelessAuthenticationSuccessHandler;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.service.AuthenticationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Logs client authentication attempts.
 */
@Aspect
@Component
public class AuthenticationLogInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLogInterceptor.class);

  private static final String USER_DETAILS_ATTR = "userDetails";

  private static final String BAD_CREDENTIALS_EXCEPTION = "BadCredentialsException";

  private static final String DISABLED_ACCOUNT_EXCEPTION = "DisabledAccountException";

  private static final String ACCOUNT_EXPIRED_EXCEPTION = "AccountExpiredException";

  private static final String ACCOUNT_LOCKED_EXCEPTION = "AccountLockedException";

  private static final String CREDENTIALS_EXCEPTION = "CredentialsException";

  private final AuthenticationLogService service;

  private final HttpServletRequest request;

  public AuthenticationLogInterceptor(AuthenticationLogService service, HttpServletRequest request) {
    this.service = service;
    this.request = request;
  }

  @Pointcut("@annotation(com.platform.aspect.annotation.LogAuthentication) && execution(* *(..))")
  public void pointCut() {
  }

  @Before("pointCut()")
  public void logInput(JoinPoint joinPoint) {
    try {
      Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
      Class<?> caller = method.getDeclaringClass();

      if (StatelessAuthenticationFailureHandler.class.equals(caller)) {
        logAuthenticationFailure(joinPoint.getArgs());
      }

      if (StatelessAuthenticationSuccessHandler.class.equals(caller)) {
        logAuthenticationSuccess();
      }
    } catch (Exception e) {
      LOGGER.error("Authentication log failed with error!", e);
    }

  }

  private void logAuthenticationFailure(Object[] args) {
    if (args == null || args.length == 0) {
      return;
    }

    Exception exception = extractException(args);

    AuthenticationLogInfo info = new AuthenticationLogInfo()
        .withStatus(AuthenticationStatus.FAILURE)
        .withResolved(Boolean.TRUE);

    switch (exception.getClass().getSimpleName()) {
      case BAD_CREDENTIALS_EXCEPTION, CREDENTIALS_EXCEPTION -> info.withReason(AuthenticationStatusReason.BAD_CREDENTIALS);
      case DISABLED_ACCOUNT_EXCEPTION, ACCOUNT_EXPIRED_EXCEPTION -> info.withReason(AuthenticationStatusReason.ACCOUNT_EXPIRED);
      case ACCOUNT_LOCKED_EXCEPTION -> info.withReason(AuthenticationStatusReason.ACCOUNT_LOCKED);
      default -> info.withReason(AuthenticationStatusReason.UNKNOWN);
    }

    ClientUserDetails clientUserDetails = extractUserDetailsFromRequest();

    AuthenticationLogEntity authenticationLog = prepareAuthenticationLogBase(clientUserDetails.client());
    authenticationLog.setAuthenticationStatus(info.getStatus());
    authenticationLog.setStatusReason(info.getReason());
    authenticationLog.setStatusResolved(info.getResolved());

    service.logAuthenticationResult(authenticationLog);
  }

  private void logAuthenticationSuccess() {
    ClientUserDetails clientUserDetails = extractUserDetailsFromRequest();
    AuthenticationLogEntity authenticationLog = prepareAuthenticationLogBase(clientUserDetails.client());
    authenticationLog.setAuthenticationStatus(AuthenticationStatus.AUTHORIZED);
    authenticationLog.setStatusReason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION);
    authenticationLog.setStatusResolved(Boolean.TRUE);

    service.logAuthenticationResult(authenticationLog);
  }

  private ClientUserDetails extractUserDetailsFromRequest() {
    return (ClientUserDetails) request.getAttribute(USER_DETAILS_ATTR);
  }

  private Exception extractException(Object[] args) {

    for (Object argument : args) {
      if (argument instanceof Exception exception) {
        return exception;
      }
    }

    throw new IllegalArgumentException("Failed to extract exception or one did not exist at all!");
  }

  private AuthenticationLogEntity prepareAuthenticationLogBase(ClientEntity client) {
    AuthenticationLogEntity authenticationLog = new AuthenticationLogEntity();
    authenticationLog.setClient(client);
    return authenticationLog;
  }

  private static class AuthenticationLogInfo {

    private AuthenticationStatusReason reason;

    private AuthenticationStatus status;

    private Boolean resolved;

    public AuthenticationStatusReason getReason() {
      return reason;
    }

    public AuthenticationLogInfo withReason(AuthenticationStatusReason reason) {
      this.reason = reason;
      return this;
    }

    public AuthenticationStatus getStatus() {
      return status;
    }

    public AuthenticationLogInfo withStatus(AuthenticationStatus status) {
      this.status = status;
      return this;
    }

    public Boolean getResolved() {
      return resolved;
    }

    public AuthenticationLogInfo withResolved(Boolean resolved) {
      this.resolved = resolved;
      return this;
    }
  }

}

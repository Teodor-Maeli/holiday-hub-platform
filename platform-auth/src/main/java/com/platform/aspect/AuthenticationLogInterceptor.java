package com.platform.aspect;

import com.platform.AuthenticationLogFacts;
import com.platform.aspect.annotation.LogAuthentication;
import com.platform.config.PlatformAuthenticationFailureHandler;
import com.platform.config.PlatformAuthenticationSuccessHandler;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.service.AuthenticationLogService;
import com.platform.util.ObjectsHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Logs client authentication attempts into the database.
 * Applies auto-lock if exhaust maximum BadCredentials attempts.
 */
@Aspect
@Component
public class AuthenticationLogInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLogInterceptor.class);

  private static final String USER_DETAILS_ATTR = "userDetails";
  private static final String BAD_CREDENTIALS_EXCEPTION = "BadCredentialsException";
  private static final String DISABLED_ACCOUNT_EXCEPTION = "DisabledAccountException";
  private static final String ACCOUNT_LOCKED_EXCEPTION = "AccountLockedException";
  private static final String LOCKED_EXCEPTION = "LockedException";

  private static final List<AuthenticationStatusReason> DISALLOWED_REASONS;

  static {
    DISALLOWED_REASONS = new ArrayList<>();
    DISALLOWED_REASONS.add(AuthenticationStatusReason.ACCOUNT_DISABLED);
    DISALLOWED_REASONS.add(AuthenticationStatusReason.ACCOUNT_HACKED);
    DISALLOWED_REASONS.add(AuthenticationStatusReason.ACCOUNT_LOCKED);
  }

  private final AuthenticationLogService service;
  private final HttpServletRequest request;

  @Value("${platform.security.accounts.auto-locking.bad-credentials.max-consecutive}")
  private Integer badCredentialsMaxAttempts;

  @Value("${platform.security.accounts.auto-locking.bad-credentials.expiry-time}")
  private Integer badCredentialsExpiryTime;

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
      LogAuthentication annotation = method.getAnnotation(LogAuthentication.class);
      Class<?> caller = method.getDeclaringClass();
      ClientUserDetails clientUserDetails = extractUserDetailsFromRequest();
      Runnable loggingJob = () -> logAuthenticationResult(caller, clientUserDetails, joinPoint.getArgs());

      if (annotation.async()) {
        CompletableFuture.runAsync(loggingJob);
      } else {
        loggingJob.run();
      }

    } catch (Exception e) {
      LOGGER.error("Authentication log failed with error!", e);
    }

  }


  private void logAuthenticationResult(Class<?> caller, ClientUserDetails clientUserDetails, Object[] args) {
    if (clientUserDetails == null) {
      return;
    }

    if (isSuccess(caller)) {
      logAuthenticationFailure(clientUserDetails, args);
    }

    if (isFailure(caller)) {
      logAuthenticationSuccess(clientUserDetails);
    }
  }

  private boolean isFailure(Class<?> caller) {
    return PlatformAuthenticationSuccessHandler.class.equals(caller);
  }

  private boolean isSuccess(Class<?> caller) {
    return PlatformAuthenticationFailureHandler.class.equals(caller);
  }

  private void logAuthenticationSuccess(ClientUserDetails clientUserDetails) {

    AuthenticationLogEntity entry = AuthenticationLogFacts.initialize()
        .withStatus(AuthenticationStatus.AUTHORIZED)
        .withReason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION)
        .withClientDetails(clientUserDetails)
        .withStatusResolved(Boolean.TRUE)
        .toEntity();

    service.logAuthenticationResult(entry);
  }

  private void logAuthenticationFailure(ClientUserDetails clientUserDetails, Object[] args) {
    if (ObjectsHelper.isEmpty(args)) {
      return;
    }

    Exception exception = extractException(args);

    AuthenticationLogEntity entry = prepareAuthenticationLogEntry(exception, clientUserDetails);
    service.logAuthenticationResult(entry);
  }

  private AuthenticationLogEntity prepareAuthenticationLogEntry(Exception exception, ClientUserDetails clientUserDetails) {
    AuthenticationStatusReason authenticationStatusReason = determineReason(exception);
    Boolean statusResolved = determineStatusResolved(clientUserDetails, authenticationStatusReason);

    return AuthenticationLogFacts.initialize()
        .withStatus(AuthenticationStatus.FAILURE)
        .withReason(authenticationStatusReason)
        .withClientDetails(clientUserDetails)
        .withStatusResolved(statusResolved)
        .toEntity();
  }

  private Boolean determineStatusResolved(ClientUserDetails clientUserDetails, AuthenticationStatusReason inputStatusReason) {
    List<AuthenticationLogEntity> logs = Optional.ofNullable(clientUserDetails.client())
        .map(ClientEntity::getAuthenticationLogs)
        .orElse(Collections.emptyList());

    if (DISALLOWED_REASONS.contains(inputStatusReason)) {
      return false;
    }

    List<AuthenticationLogEntity> badCredentialsLogs = getNonExpiredBadCredentials(logs);
    return badCredentialsLogs.size() < badCredentialsMaxAttempts;
  }

  private List<AuthenticationLogEntity> getNonExpiredBadCredentials(List<AuthenticationLogEntity> logs) {
    return logs.stream()
        .filter(this::isNonExpiredBadCredentialLog)
        .toList();
  }

  private boolean isNonExpiredBadCredentialLog(AuthenticationLogEntity log) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.minusMinutes(badCredentialsExpiryTime);

    return log.getStatusReason().equals(AuthenticationStatusReason.BAD_CREDENTIALS)
        && log.getCreatedDate().isBefore(now) && log.getCreatedDate().isAfter(expirationTime);
  }

  private AuthenticationStatusReason determineReason(Exception exception) {
    return switch (exception.getClass().getSimpleName()) {
      case BAD_CREDENTIALS_EXCEPTION -> AuthenticationStatusReason.BAD_CREDENTIALS;
      case DISABLED_ACCOUNT_EXCEPTION -> AuthenticationStatusReason.ACCOUNT_DISABLED;
      case ACCOUNT_LOCKED_EXCEPTION, LOCKED_EXCEPTION -> AuthenticationStatusReason.ACCOUNT_LOCKED;
      default -> AuthenticationStatusReason.UNKNOWN;
    };
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

}

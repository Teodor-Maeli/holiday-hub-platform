package com.platform.aspect;

import com.platform.AuthenticationLogFacts;
import com.platform.config.PlatformAuthenticationFailureHandler;
import com.platform.config.PlatformAuthenticationSuccessHandler;
import com.platform.config.PlatformSecurityProperties;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.CustomerEntity;
import com.platform.service.AuthenticationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.platform.model.AuthenticationStatusReason.ACCOUNT_DISABLED;
import static com.platform.model.AuthenticationStatusReason.ACCOUNT_HACKED;
import static com.platform.model.AuthenticationStatusReason.ACCOUNT_LOCKED;
import static com.platform.model.AuthenticationStatusReason.BAD_CREDENTIALS;
import static com.platform.model.AuthenticationStatusReason.UNKNOWN;

/**
 * Logs client authentication attempts into the database.
 * Applies auto-lock if exhaust maximum BadCredentials attempts.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationLogInterceptor {

  private static final String USER_DETAILS_ATTR = "userDetails";
  private static final String BAD_CREDENTIALS_EXCEPTION = "BadCredentialsException";
  private static final String DISABLED_ACCOUNT_EXCEPTION = "DisabledAccountException";
  private static final String ACCOUNT_LOCKED_EXCEPTION = "AccountLockedException";
  private static final String LOCKED_EXCEPTION = "LockedException";

  private static final List<AuthenticationStatusReason> DISALLOWED_REASONS = List.of(
      ACCOUNT_DISABLED, ACCOUNT_HACKED, ACCOUNT_LOCKED);

  private final PlatformSecurityProperties properties;
  private final AuthenticationLogService service;
  private final HttpServletRequest request;


  @Pointcut("@annotation(com.platform.aspect.LogAuthentication) && execution(* *(..))")
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
      log.error("Authentication log failed with error!", e);
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
        .status(AuthenticationStatus.AUTHORIZED)
        .reason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION)
        .clientDetails(clientUserDetails)
        .resolved(Boolean.TRUE)
        .toEntity();

    service.logAuthenticationResult(entry);
  }

  private void logAuthenticationFailure(ClientUserDetails clientUserDetails, Object[] args) {
    if (!ObjectUtils.isEmpty(args)) {
      Exception exception = extractException(args);
      AuthenticationLogEntity entry = prepareAuthenticationLogEntry(exception, clientUserDetails);
      service.logAuthenticationResult(entry);
    }
  }

  private AuthenticationLogEntity prepareAuthenticationLogEntry(Exception exception, ClientUserDetails clientUserDetails) {
    AuthenticationStatusReason authenticationStatusReason = determineReason(exception);
    Boolean statusResolved = isStatusResolved(clientUserDetails, authenticationStatusReason);

    return AuthenticationLogFacts.initialize()
        .status(AuthenticationStatus.FAILURE)
        .reason(authenticationStatusReason)
        .clientDetails(clientUserDetails)
        .resolved(statusResolved)
        .toEntity();
  }

  private Boolean isStatusResolved(ClientUserDetails clientUserDetails, AuthenticationStatusReason inputStatusReason) {
    List<AuthenticationLogEntity> logs = Optional.ofNullable(clientUserDetails.client())
        .map(CustomerEntity::getAuthenticationLogs)
        .orElse(Collections.emptyList());

    if (DISALLOWED_REASONS.contains(inputStatusReason)) {
      return false;
    }

    List<AuthenticationLogEntity> badCredentialsLogs = getNonExpiredBadCredentials(logs);
    return badCredentialsLogs.size() <= properties.getBadCredentialsExpiryTime();
  }

  private List<AuthenticationLogEntity> getNonExpiredBadCredentials(List<AuthenticationLogEntity> logs) {
    return logs.stream()
        .filter(this::isNonExpiredBadCredentialLog)
        .toList();
  }

  private boolean isNonExpiredBadCredentialLog(AuthenticationLogEntity log) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.minusMinutes(properties.getBadCredentialsExpiryTime());

    return log.getStatusReason().equals(BAD_CREDENTIALS)
        && log.getCreatedDate().isBefore(now) && log.getCreatedDate().isAfter(expirationTime);
  }

  private AuthenticationStatusReason determineReason(Exception exception) {
    return switch (exception.getClass().getSimpleName()) {
      case BAD_CREDENTIALS_EXCEPTION -> BAD_CREDENTIALS;
      case DISABLED_ACCOUNT_EXCEPTION -> ACCOUNT_DISABLED;
      case ACCOUNT_LOCKED_EXCEPTION, LOCKED_EXCEPTION -> ACCOUNT_LOCKED;
      default -> UNKNOWN;
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

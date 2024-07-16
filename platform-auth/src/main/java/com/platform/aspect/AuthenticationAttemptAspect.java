package com.platform.aspect;

import com.platform.config.PlatformAuthenticationFailureHandler;
import com.platform.config.PlatformAuthenticationSuccessHandler;
import com.platform.config.PlatformSecurityProperties;
import com.platform.model.AuthenticationAttemptResource;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.CustomerResource;
import com.platform.model.CustomerUserDetails;
import com.platform.service.AuthenticationAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.security.auth.login.AccountLockedException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.platform.model.AuthenticationStatusReason.ACCOUNT_DISABLED;
import static com.platform.model.AuthenticationStatusReason.ACCOUNT_HACKED;
import static com.platform.model.AuthenticationStatusReason.ACCOUNT_LOCKED;
import static com.platform.model.AuthenticationStatusReason.BAD_CREDENTIALS;
import static com.platform.model.AuthenticationStatusReason.UNKNOWN;

/**
 * Creates customer authentication attempts into the database.
 * Applies auto-lock if exhaust maximum BadCredentials attempts.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAttemptAspect {

  private static final String USER_DETAILS_ATTR = "userDetails";
  private static final List<AuthenticationStatusReason> DISALLOWED_REASONS = List.of(ACCOUNT_DISABLED, ACCOUNT_HACKED, ACCOUNT_LOCKED);

  private final PlatformSecurityProperties properties;
  private final AuthenticationAttemptService service;
  private final HttpServletRequest request;

  @Pointcut("@annotation(com.platform.aspect.TrackAuthentication) && execution(* *(..))")
  public void pointCut() {
  }

  @Before("pointCut()")
  public void logInput(JoinPoint joinPoint) {
    try {
      Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
      TrackAuthentication annotation = method.getAnnotation(TrackAuthentication.class);
      Class<?> caller = method.getDeclaringClass();
      CustomerUserDetails customerUserDetails = extractUserDetailsFromRequest();
      Runnable recordingJob = () -> recordAttempt(caller, customerUserDetails, joinPoint.getArgs());

      if (annotation.async()) {
        CompletableFuture.runAsync(recordingJob);
      } else {
        recordingJob.run();
      }

    } catch (Exception e) {
      log.error("Authentication attempt record failed with error!", e);
    }

  }


  private void recordAttempt(Class<?> caller, CustomerUserDetails customerUserDetails, Object[] args) {
    if (customerUserDetails == null) {
      return;
    }

    if (isSuccess(caller)) {
      recordSuccess(customerUserDetails);
    }

    if (isFailure(caller)) {
      recordFailure(customerUserDetails, args);
    }
  }

  private boolean isFailure(Class<?> caller) {
    return PlatformAuthenticationFailureHandler.class.equals(caller);
  }

  private boolean isSuccess(Class<?> caller) {
    return PlatformAuthenticationSuccessHandler.class.equals(caller);
  }

  private void recordSuccess(CustomerUserDetails customerUserDetails) {

    AuthenticationAttemptResource resource = new AuthenticationAttemptResource();
    resource.setAuthenticationStatus(AuthenticationStatus.AUTHORIZED);
    resource.setStatusReason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION);
    resource.setStatusResolved(Boolean.TRUE);

    service.recordAttempt(resource, customerUserDetails.customer());
  }

  private void recordFailure(CustomerUserDetails customerUserDetails, Object[] args) {
    if (!ObjectUtils.isEmpty(args)) {
      Exception exception = extractException(args);
      AuthenticationAttemptResource resource = prepareFailureEntity(exception, customerUserDetails);
      service.recordAttempt(resource, customerUserDetails.customer());
    }
  }

  private AuthenticationAttemptResource prepareFailureEntity(Exception exception, CustomerUserDetails customerUserDetails) {
    AuthenticationStatusReason authenticationStatusReason = determineReason(exception);
    Boolean statusResolved = isStatusResolved(customerUserDetails, authenticationStatusReason);

    AuthenticationAttemptResource resource = new AuthenticationAttemptResource();
    resource.setAuthenticationStatus(AuthenticationStatus.FAILURE);
    resource.setStatusReason(authenticationStatusReason);
    resource.setStatusResolved(statusResolved);

    return resource;
  }

  private Boolean isStatusResolved(CustomerUserDetails customerUserDetails, AuthenticationStatusReason inputStatusReason) {
    List<AuthenticationAttemptResource> attempts = Optional.ofNullable(customerUserDetails.customer())
        .map(CustomerResource::getAuthenticationAttempts)
        .orElse(Collections.emptyList());

    if (DISALLOWED_REASONS.contains(inputStatusReason)) {
      return false;
    }

    return getNonExpiredBadCredentials(attempts).size() <= properties.getMaxConsecutiveBadCredentials();
  }

  private List<AuthenticationAttemptResource> getNonExpiredBadCredentials(List<AuthenticationAttemptResource> attempts) {
    return attempts.stream()
        .filter(this::isNonExpiredBadCredentialAttempt)
        .toList();
  }

  private boolean isNonExpiredBadCredentialAttempt(AuthenticationAttemptResource attempt) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.minusMinutes(properties.getBadCredentialsExpiryTime());

    return attempt.getStatusReason().equals(BAD_CREDENTIALS)
        && attempt.getCreatedDate().isBefore(now) && attempt.getCreatedDate().isAfter(expirationTime);
  }

  private AuthenticationStatusReason determineReason(Exception exception) {
    return switch (exception) {
      case BadCredentialsException badCredentials -> BAD_CREDENTIALS;
      case DisabledException disabled -> ACCOUNT_DISABLED;
      case AccountLockedException accountLocked -> ACCOUNT_LOCKED;
      case LockedException locked -> ACCOUNT_LOCKED;
      case null, default -> UNKNOWN;
    };
  }

  private CustomerUserDetails extractUserDetailsFromRequest() {
    return (CustomerUserDetails) request.getAttribute(USER_DETAILS_ATTR);
  }

  private Exception extractException(Object[] arguments) {
    return Stream.of(arguments).filter(argument -> argument instanceof Exception)
        .findFirst()
        .map(Exception.class::cast)
        .orElseThrow(() -> new IllegalArgumentException("Failed to extract exception or one did not exist at all!"));
  }

}

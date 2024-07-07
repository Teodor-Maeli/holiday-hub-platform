package com.platform.aspect;

import com.platform.AuthenticationDetails;
import com.platform.config.PlatformAuthenticationFailureHandler;
import com.platform.config.PlatformAuthenticationSuccessHandler;
import com.platform.config.PlatformSecurityProperties;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.CustomerUserDetails;
import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.entity.Customer;
import com.platform.service.AuthenticationAttemptService;
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
import java.util.stream.Stream;

import static com.platform.model.AuthenticationStatusReason.ACCOUNT_DISABLED;
import static com.platform.model.AuthenticationStatusReason.ACCOUNT_HACKED;
import static com.platform.model.AuthenticationStatusReason.ACCOUNT_LOCKED;
import static com.platform.model.AuthenticationStatusReason.BAD_CREDENTIALS;
import static com.platform.model.AuthenticationStatusReason.UNKNOWN;

/**
 * Creates client authentication attempts into the database.
 * Applies auto-lock if exhaust maximum BadCredentials attempts.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAttemptAspect {

  private static final String USER_DETAILS_ATTR = "userDetails";
  private static final String BAD_CREDENTIALS_EXCEPTION = "BadCredentialsException";
  private static final String DISABLED_ACCOUNT_EXCEPTION = "DisabledAccountException";
  private static final String ACCOUNT_LOCKED_EXCEPTION = "AccountLockedException";
  private static final String LOCKED_EXCEPTION = "LockedException";

  private static final List<AuthenticationStatusReason> DISALLOWED_REASONS = List.of(
      ACCOUNT_DISABLED, ACCOUNT_HACKED, ACCOUNT_LOCKED);

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
      recordFailure(customerUserDetails, args);
    }

    if (isFailure(caller)) {
      recordSuccess(customerUserDetails);
    }
  }

  private boolean isFailure(Class<?> caller) {
    return PlatformAuthenticationSuccessHandler.class.equals(caller);
  }

  private boolean isSuccess(Class<?> caller) {
    return PlatformAuthenticationFailureHandler.class.equals(caller);
  }

  private void recordSuccess(CustomerUserDetails customerUserDetails) {

    AuthenticationAttempt entity = AuthenticationDetails.create()
        .status(AuthenticationStatus.AUTHORIZED)
        .reason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION)
        .clientDetails(customerUserDetails)
        .resolved(Boolean.TRUE)
        .toEntity();

    service.recordAttempt(entity);
  }

  private void recordFailure(CustomerUserDetails customerUserDetails, Object[] args) {
    if (!ObjectUtils.isEmpty(args)) {
      Exception exception = extractException(args);
      AuthenticationAttempt entry = prepareFailureEntity(exception, customerUserDetails);
      service.recordAttempt(entry);
    }
  }

  private AuthenticationAttempt prepareFailureEntity(Exception exception, CustomerUserDetails customerUserDetails) {
    AuthenticationStatusReason authenticationStatusReason = determineReason(exception);
    Boolean statusResolved = isStatusResolved(customerUserDetails, authenticationStatusReason);

    return AuthenticationDetails.create()
        .status(AuthenticationStatus.FAILURE)
        .reason(authenticationStatusReason)
        .clientDetails(customerUserDetails)
        .resolved(statusResolved)
        .toEntity();
  }

  private Boolean isStatusResolved(CustomerUserDetails customerUserDetails, AuthenticationStatusReason inputStatusReason) {
    List<AuthenticationAttempt> attempts = Optional.ofNullable(customerUserDetails.client())
        .map(Customer::getAuthenticationAttempts)
        .orElse(Collections.emptyList());

    if (DISALLOWED_REASONS.contains(inputStatusReason)) {
      return false;
    }

    return getNonExpiredBadCredentials(attempts).size() <= properties.getBadCredentialsExpiryTime();
  }

  private List<AuthenticationAttempt> getNonExpiredBadCredentials(List<AuthenticationAttempt> attempts) {
    return attempts.stream()
        .filter(this::isNonExpiredBadCredentialAttempt)
        .toList();
  }

  private boolean isNonExpiredBadCredentialAttempt(AuthenticationAttempt attempt) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expirationTime = now.minusMinutes(properties.getBadCredentialsExpiryTime());

    return attempt.getStatusReason().equals(BAD_CREDENTIALS)
        && attempt.getCreatedDate().isBefore(now) && attempt.getCreatedDate().isAfter(expirationTime);
  }

  private AuthenticationStatusReason determineReason(Exception exception) {
    return switch (exception.getClass().getSimpleName()) {
      case BAD_CREDENTIALS_EXCEPTION -> BAD_CREDENTIALS;
      case DISABLED_ACCOUNT_EXCEPTION -> ACCOUNT_DISABLED;
      case ACCOUNT_LOCKED_EXCEPTION, LOCKED_EXCEPTION -> ACCOUNT_LOCKED;
      default -> UNKNOWN;
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

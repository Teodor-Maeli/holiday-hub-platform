package com.platform.aspect;

import com.platform.AuthenticationLogFacts;
import com.platform.config.StatelessAuthenticationFailureHandler;
import com.platform.config.StatelessAuthenticationSuccessHandler;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
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
import java.time.LocalDateTime;
import java.util.List;

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

  private static final String ACCOUNT_LOCKED_EXCEPTION = "AccountLockedException";

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

  private void logAuthenticationSuccess() {
    ClientUserDetails clientUserDetails = extractUserDetailsFromRequest();

    AuthenticationLogEntity log = AuthenticationLogFacts.initialize()
        .withStatus(AuthenticationStatus.AUTHORIZED)
        .withReason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION)
        .withClientDetails(clientUserDetails)
        .withStatusResolved(Boolean.TRUE)
        .toEntity();

    service.logAuthenticationResult(log);
  }

  private void logAuthenticationFailure(Object[] args) {
    if (args == null || args.length == 0) {
      return;
    }

    ClientUserDetails clientUserDetails = extractUserDetailsFromRequest();
    Exception exception = extractException(args);

    AuthenticationLogEntity authenticationLog = prepareAuthenticationLogEntry(exception, clientUserDetails);
    service.logAuthenticationResult(authenticationLog);
  }

  private AuthenticationLogEntity prepareAuthenticationLogEntry(Exception exception, ClientUserDetails clientUserDetails) {
    return AuthenticationLogFacts.initialize()
        .withStatus(AuthenticationStatus.FAILURE)
        .withReason(determineReason(exception))
        .withClientDetails(clientUserDetails)
        .withStatusResolved(determineStatusResolved(clientUserDetails))
        .toEntity();
  }

  private Boolean determineStatusResolved(ClientUserDetails clientUserDetails) {
    List<AuthenticationLogEntity> logs = clientUserDetails.client().getAuthenticationLogs();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime last30Minutes = now.minusMinutes(30);

    if (logs == null || logs.isEmpty()) {
      return true;
    }


    List<AuthenticationLogEntity> filteredLogs = logs.stream()
        .filter(log -> log.getStatusReason().equals(AuthenticationStatusReason.BAD_CREDENTIALS))
        .filter(log -> log.getCreatedDate().isBefore(now) && log.getCreatedDate().isAfter(last30Minutes))
        .toList();

    boolean isResolved = filteredLogs.size() < 3;


    if (!isResolved) {
      LOGGER.warn("Client might be locked for the next 1h!");
    }

    return isResolved;
  }

  private AuthenticationStatusReason determineReason(Exception exception) {
    return switch (exception.getClass().getSimpleName()) {
      case BAD_CREDENTIALS_EXCEPTION -> AuthenticationStatusReason.BAD_CREDENTIALS;
      case DISABLED_ACCOUNT_EXCEPTION -> AuthenticationStatusReason.ACCOUNT_DISABLED;
      case ACCOUNT_LOCKED_EXCEPTION -> AuthenticationStatusReason.ACCOUNT_LOCKED;
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

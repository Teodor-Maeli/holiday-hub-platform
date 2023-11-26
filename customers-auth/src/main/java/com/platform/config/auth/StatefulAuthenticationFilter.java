package com.platform.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.util.ImprovedJdbcTemplate;
import com.platform.domain.entity.Client;
import com.platform.exception.SessionFailureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This filter should handle persist of new sessions and invalidate the old ones.
 * This class being a bean, benefit us from DI.
 */
@Component(value = "authenticationFilter")
@DependsOn(value = "improvedJdbcTemplate")
public class StatefulAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatefulAuthenticationFilter.class);

  private static final String Q_INSERT_SESSION = """
      INSERT INTO PUBLIC.SESSION (
          SESSION_ID,
          CLIENT_ID,
          INITIATED_DATE,
          ACTIVE,
          INITIATED_BY)
      VALUES (?,?,?,true,?)
      """;

  private static final String Q_INVALIDATE_SESSION = """
      UPDATE PUBLIC.SESSION SET
          ACTIVE = false,
          EVICTED_DATE = ?,
          EVICTED_BY = ?
      WHERE CLIENT_ID = ?
      AND SESSION_ID <> ?
      AND ACTIVE = true
      """;

  private final SessionRegistry sessionRegistry;

  private final SecurityContextRepository securityContextRepository;

  private final ObjectMapper objectMapper;

  private final ImprovedJdbcTemplate jdbc;

  public StatefulAuthenticationFilter(
      SessionRegistry sessionRegistry,
      AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository,
      ObjectMapper objectMapper,
      ImprovedJdbcTemplate jdbc
  ) {
    super(authenticationManager);
    this.sessionRegistry = sessionRegistry;
    this.securityContextRepository = securityContextRepository;
    this.objectMapper = objectMapper;
    this.jdbc = jdbc;
  }

  @PostConstruct
  private void configure() {
    setSecurityContextRepository(securityContextRepository);
    setAuthenticationFailureHandler(new StatefulAuthenticationFailureHandler(objectMapper));
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    return request.getHeader("password");
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return request.getHeader("username");
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authResult) throws ServletException, IOException {
    String freshSessionId = request.getSession().getId();
    Client client = (Client) authResult.getPrincipal();

    try {
      persistNew(client, freshSessionId);
      super.successfulAuthentication(request, response, chain, authResult);
    } catch (RuntimeException e) {
      invalidateFreshlyCachedSession(freshSessionId);
      getFailureHandler().onAuthenticationFailure(request, response, translateException(e));
    } finally {
      invalidateOld(client, freshSessionId); // only one allowed
    }
  }

  private void persistNew(Client client, String freshSessionId) {
    sessionRegistry.registerNewSession(freshSessionId, client);

    try {
      int updated = jdbc.update(
          Q_INSERT_SESSION,
          freshSessionId,
          client.getId(),
          LocalDateTime.now(),
          client.getUsername()
      );

      jdbc.commit();
      LOGGER.info("Successfully invalidated old sessions for client {} and {} rows affected.", client.getId(), updated);
    } catch (RuntimeException e) {
      jdbc.rollback();
      throw e;
    } finally {
      jdbc.close();
    }
  }

  private void invalidateOld(Client client, String freshSessionId) {
    sessionRegistry.getAllSessions(client, false)
        .stream()
        .filter(session -> ! session.getSessionId().equals(freshSessionId))
        .forEach(SessionInformation::expireNow);

    try {
      int updated = jdbc.update(
          Q_INVALIDATE_SESSION,
          LocalDateTime.now(),
          client.getUsername(),
          client.getId(),
          freshSessionId
      );

      jdbc.commit();
      LOGGER.info("Successfully invalidated old sessions for client {} and {} rows affected.", client.getId(), updated);
    } catch (RuntimeException e) {
      jdbc.rollback();
      throw e;
    } finally {
      jdbc.close();
    }
  }

  private void invalidateFreshlyCachedSession(String sessionId) {
    SessionInformation session = sessionRegistry.getSessionInformation(sessionId);

    if (session != null) {
      session.expireNow();
    }
  }

  private AuthenticationException translateException(Exception e) {
    if (e instanceof DuplicateKeyException) {
      return new SessionFailureException(e.getMessage(), e.getCause(), HttpStatus.FOUND);
    }

    return new SessionFailureException(e.getMessage(), e.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

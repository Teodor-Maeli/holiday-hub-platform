package com.platform.config.auth;

import com.platform.domain.entity.Client;
import com.platform.exception.BackendException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * This filter should handle persist of new sessions and invalidate old as there should be only one active per customer!
 */
@Component(value = "authenticationFilter")
public class StatefulAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatefulAuthenticationFilter.class);

  private static final String Q_INSERT_SESSION = """
      INSERT INTO PUBLIC.SESSION (
          SESSION_ID,
          CLIENT_ID,
          INITIATED_DATE,
          ACTIVE,
          INITIATED_BY)
      VALUES (?,?,?,?,?)
      """;

  private static final String Q_INVALIDATE_SESSION = """
      UPDATE PUBLIC.SESSION SET
          ACTIVE = ?,
          EVICTED_DATE = ?,
          EVICTED_BY = ?
      WHERE CLIENT_ID = ?
      AND SESSION_ID <> ?
      """;

  private final JdbcTemplate jdbc;

  private final SessionRegistry sessionRegistry;

  public StatefulAuthenticationFilter(
      JdbcTemplate jdbc,
      SessionRegistry sessionRegistry,
      AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository
  ) {
    super(authenticationManager);
    this.jdbc = jdbc;
    this.sessionRegistry = sessionRegistry;
    setSecurityContextRepository(securityContextRepository);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    SessionInformation sessionInformation = sessionRegistry.getSessionInformation(request.changeSessionId());

    if (sessionInformation == null) {
      return super.attemptAuthentication(request, response);
    }

    throw new BackendException("User is already authenticated with current session!", HttpStatus.FOUND);
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

    Object principal = authResult.getPrincipal();
    Client client = (Client) principal;
    String freshSessionId = request.getSession().getId();

    persistNew(client, freshSessionId);
    invalidateOld(client, freshSessionId); // only one allowed
    super.successfulAuthentication(request, response, chain, authResult);
  }

  private void persistNew(Client client, String freshSessionId) {
    try {
      sessionRegistry.registerNewSession(freshSessionId, client);

      jdbc.update(
          Q_INSERT_SESSION,
          freshSessionId,
          client.getId(),
          LocalDateTime.now(),
          Boolean.TRUE,
          client.getUsername()
      );
      LOGGER.info("Successfully persisted session into the DB!");
    } catch (RuntimeException e) {
      invalidateFreshlyCachedSession(freshSessionId);
      throw new BackendException("Session persistence failed for client " + client.getId() + " ! Invalidated session from cache!",
          HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  private void invalidateOld(Client client, String freshSessionId) {
    try {
      sessionRegistry.getAllSessions(client, false)
          .stream()
          .filter(session -> ! session.getSessionId().equals(freshSessionId))
          .forEach(SessionInformation::expireNow);

      int updated = jdbc.update(
          Q_INVALIDATE_SESSION,
          Boolean.FALSE,
          LocalDateTime.now(),
          client.getUsername(),
          client.getId(),
          freshSessionId
      );

      LOGGER.warn("{} rows has been updated when invalidating old sessions!", updated);

    } catch (RuntimeException e) {
      LOGGER.error("Session update failed for client {} ! There might be more then one active session!", client.getId(), e);
    }
  }

  private void invalidateFreshlyCachedSession(String sessionId) {
    SessionInformation session = sessionRegistry.getSessionInformation(sessionId);

    if (session != null) {
      session.expireNow();
    }
  }
}

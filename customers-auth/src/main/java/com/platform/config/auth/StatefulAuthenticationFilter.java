package com.platform.config.auth;

import com.platform.domain.entity.Client;
import com.platform.exception.BackendException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
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
          ACTIVE = false,
          EVICTED_DATE = ?,
          EVICTED_BY = ?
      WHERE CLIENT_ID = ?
      AND SESSION_ID <> ?
      AND ACTIVE = true
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

    try {
      Client client = (Client) authResult.getPrincipal();
      String freshSessionId = request.getSession().getId();

      persistNew(client, response, freshSessionId);
      invalidateOld(client, freshSessionId); // only one allowed
      super.successfulAuthentication(request, response, chain, authResult);
    } catch (Exception e) {
      //probably we need extend this handler and register ours, this is not going to work.
      super.getFailureHandler().onAuthenticationFailure(request, response, new AuthenticationServiceException(e.getMessage(), e.getCause()));
    }
  }

  private void persistNew(Client client, HttpServletResponse response, String freshSessionId) {
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

      if (e instanceof DuplicateKeyException) {
        //TODO this has to be improved, its working but need to be synchronized with the failure handler, anyway a single session is allowed.
        response.setStatus(HttpStatus.FOUND.value());
        throw e;
      } else {
        throw new BackendException("Session persistence failed for client " + client.getId() + " ! Invalidated session from cache!",
            HttpStatus.INTERNAL_SERVER_ERROR, e);
      }

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
          LocalDateTime.now(),
          client.getUsername(),
          client.getId(),
          freshSessionId
      );

      LOGGER.info("{} rows has been updated when invalidating old sessions!", updated);

    } catch (RuntimeException e) {
      LOGGER.error("Session invalidation failed for client {} !", client.getId(), e);
    }
  }

  private void invalidateFreshlyCachedSession(String sessionId) {
    SessionInformation session = sessionRegistry.getSessionInformation(sessionId);

    if (session != null) {
      session.expireNow();
    }
  }
}

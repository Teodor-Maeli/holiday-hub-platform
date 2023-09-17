package com.platform.config.auth;

import com.platform.domain.entity.Client;
import com.platform.exception.BackendException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
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

@Component(value = "authenticationFilter")
public class StatefulAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatefulAuthenticationFilter.class);
    private static final String Q_INSERT_SESSION = """
        INSERT INTO PUBLIC.SESSION (
            SESSION_ID,
            CLIENT_ID,
            INITIATED_DATE,
            EVICTED_DATE,
            ACTIVE,
            INITIATED_BY,
            EVICTED_BY)
        VALUES (?,?,?,?,?,?,?)
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
        String sessionId = request.getSession().getId();

        persistSession(client, sessionId);
        super.successfulAuthentication(request, response, chain, authResult);
    }

    private void persistSession(Client client, String sessionId) {
        try {
            sessionRegistry.registerNewSession(sessionId, client);

            jdbc.update(Q_INSERT_SESSION,
                sessionId,
                client.getId(),
                LocalDateTime.now(),
                null,
                Boolean.TRUE,
                client.getUsername(),
                null);
            LOGGER.info("Successfully persisted session into the DB!");
        } catch (RuntimeException e) {
            invalidateCachedSession(sessionId);
            throw new BackendException("Session persistence failed for client " + client.getId() + " ! Invalidated session from cache!",
                HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void invalidateCachedSession(String sessionId) {
        SessionInformation session = sessionRegistry.getSessionInformation(sessionId);

        if (session != null) {
            session.expireNow();
        }
    }
}

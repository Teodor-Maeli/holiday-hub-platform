package com.platform.config.auth;

import com.platform.domain.entity.Client;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
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
    private final SecurityContextRepository securityContextRepository;

    public StatefulAuthenticationFilter(
        JdbcTemplate jdbc,
        SessionRegistry sessionRegistry,
        AuthenticationManager authenticationManager,
        SecurityContextRepository securityContextRepository
    ) {
        super(authenticationManager);
        this.jdbc = jdbc;
        this.sessionRegistry = sessionRegistry;
        this.securityContextRepository = securityContextRepository;
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
        FilterChain chain, Authentication authResult) {

        Object principal = authResult.getPrincipal();
        String sessionId = request.getSession().getId();
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authResult);
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        securityContextHolderStrategy.setContext(context);

        securityContextRepository.saveContext(context, request, response);
        sessionRegistry.registerNewSession(sessionId, principal);

        CompletableFuture.runAsync(() -> persistSession((Client) principal, sessionId));
    }

    private void persistSession(Client client, String sessionId) {
        try {
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
            LOGGER.error("Failed to log session for customer {} in database asynchronously!", client.getId(), e);
        }
    }
}

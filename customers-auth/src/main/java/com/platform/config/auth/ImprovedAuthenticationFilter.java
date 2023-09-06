package com.platform.config.auth;

import com.platform.domain.entity.Client;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component(value = "authenticationFilter")
public class ImprovedAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImprovedAuthenticationFilter.class);
    private static final String Q_INSERT_SESSION = """
        INSERT INTO PUBLIC.SESSION (
            ID,
            CLIENT_ID,
            INITIATED_DATE,
            EVICTED_DATE,
            ACTIVE,
            INITIATED_BY,
            EVICTED_BY)
        VALUES ((SELECT nextval('SESSION_SEQ')),?,?,?,?,?,?)
        """;
    private static final String Q_ACTIVE_SESSION_EXISTS_BY_ID = """
        SELECT COUNT(S.ACTIVE)
        FROM PUBLIC.SESSION S
        WHERE S.CLIENT_ID = ?
        """;

    private final JdbcTemplate jdbc;
    private final SessionRegistry sessionRegistry;

    public ImprovedAuthenticationFilter(AuthenticationManager authenticationManager,
                                        JdbcTemplate jdbc,
                                        SessionRegistry sessionRegistry) {
        super(authenticationManager);
        this.jdbc = jdbc;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,

                                            Authentication authResult) throws IOException, ServletException {
        logSessionAsync(authResult);
        super.successfulAuthentication(request, response, chain, authResult);
    }

    private void logSessionAsync(Authentication authResult) {
        CompletableFuture.runAsync(() -> {
            Client client = (Client) authResult.getPrincipal();
            if (shouldLogSession(client)) {
                logSession(client);
            }
        });
    }

    private boolean shouldLogSession(Client client) {
        //TODO exception handling
        Integer result = jdbc.queryForObject(Q_ACTIVE_SESSION_EXISTS_BY_ID, Integer.class, client.getId());
        return result < 1;
    }


    private void logSession(Client client) {
        try {
            jdbc.update(Q_INSERT_SESSION, client.getId(), LocalDateTime.now(), null, Boolean.TRUE, client.getUsername(), null);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to log session in database for {}", client.getId(), e);
        }
    }
}

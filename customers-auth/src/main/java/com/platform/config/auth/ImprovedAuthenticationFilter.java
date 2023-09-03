package com.platform.config.auth;

import com.platform.domain.entity.Client;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component(value = "authenticationFilter")
public class ImprovedAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String Q_INSERT_SESSION_BY_USERNAME = """
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

    private final JdbcTemplate jdbc;

    public ImprovedAuthenticationFilter(AuthenticationManager authenticationManager,
                                        JdbcTemplate jdbc) {
        super(authenticationManager);
        this.jdbc = jdbc;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
        throws AuthenticationException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        logSessionInDataBase(authResult);
        super.successfulAuthentication(request, response, chain, authResult);
    }

    private void logSessionInDataBase(Authentication authResult) {
        Client client = (Client) authResult.getPrincipal();

        jdbc.update(Q_INSERT_SESSION_BY_USERNAME,
                    client.getId(),
                    LocalDateTime.now(),
                    null,
                    Boolean.TRUE,
                    client.getUsername(),
                    null);
    }
}

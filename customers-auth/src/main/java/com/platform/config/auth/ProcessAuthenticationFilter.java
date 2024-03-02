package com.platform.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.platform.model.ConsumerAuthority;
import com.platform.config.model.JWTAuthenticationToken;
import com.platform.config.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Performs a Client authentication.
 */
@Component("processAuthenticationFilter")
public class ProcessAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String ROLES = "roles";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String encodedJWT = request.getHeader(AUTHORIZATION_HEADER);

    try {
      DecodedJWT decodedJWT = verifyJWT(encodedJWT);
      String subject = decodedJWT.getSubject();
      List<ConsumerAuthority> roles = decodedJWT.getClaim(ROLES).asList(ConsumerAuthority.class);

      Set<SimpleGrantedAuthority> authorities = SecurityUtils.toSimpleGrantedAuthority(roles);
      setAuthentication(subject, authorities);

      filterChain.doFilter(request, response);

    } catch (JWTVerificationException e) {
      filterChain.doFilter(request, response);
    }

  }

  private void setAuthentication(String subject, Set<SimpleGrantedAuthority> authorities) {
    Authentication authentication = new JWTAuthenticationToken(subject, authorities);

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private DecodedJWT verifyJWT(String encodedJWT) {
    return getJWTVerifier().verify(encodedJWT);
  }

  private JWTVerifier getJWTVerifier() {
    return JWT.require(SecurityUtils.getSignAlgorithm())
        .build();
  }

}

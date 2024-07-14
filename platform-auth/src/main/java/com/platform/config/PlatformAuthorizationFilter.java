package com.platform.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.platform.model.ConsumerAuthority;
import com.platform.model.JWTAuthenticationToken;
import com.platform.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Performs a Client authorization.
 */
public class PlatformAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String encodedJWT = request.getHeader("Authorization");

    try {
      DecodedJWT decodedJWT = verifyJWT(encodedJWT);
      String subject = decodedJWT.getSubject();
      List<ConsumerAuthority> roles = decodedJWT.getClaim("roles").asList(ConsumerAuthority.class);

      Set<SimpleGrantedAuthority> authorities = SecurityUtils.toSimpleGrantedAuthorities(roles);
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

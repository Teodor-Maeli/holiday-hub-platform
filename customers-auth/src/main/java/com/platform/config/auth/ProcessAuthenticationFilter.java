package com.platform.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.platform.common.model.Role;
import com.platform.config.model.JWTAuthenticationToken;
import com.platform.config.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Performs a Client authentication.
 */
@Component("processAuthenticationFilter")
public class ProcessAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  private final JWTUtil jwtUtil;

  public ProcessAuthenticationFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String encodedJWT = request.getHeader(AUTHORIZATION_HEADER);

    try {
      DecodedJWT decodedJWT = verifyJWT(encodedJWT);
      String subject = decodedJWT.getSubject();
      List<Role> roles = decodedJWT.getClaim("roles").asList(Role.class);

      Set<SimpleGrantedAuthority> authorities = toSimpleGrantedAuthorities(roles);
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(subject, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(authToken);
      filterChain.doFilter(request, response);

    } catch (JWTVerificationException e) {
      filterChain.doFilter(request, response);
    }

  }

  private static Set<SimpleGrantedAuthority> toSimpleGrantedAuthorities(List<Role> roles) {
    if (roles == null) {
      return Collections.emptySet();
    }

    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toSet());
  }

  private DecodedJWT verifyJWT(String encodedJWT) {
    return getJWTVerifier().verify(encodedJWT);
  }

  private JWTVerifier getJWTVerifier() {
    return JWT.require(jwtUtil.getSignAlgorithm())
        .build();
  }

}

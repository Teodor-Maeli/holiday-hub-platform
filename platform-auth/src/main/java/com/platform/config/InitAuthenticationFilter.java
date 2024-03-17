package com.platform.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Initiates a Client authentication.
 */
@Component(value = "initAuthenticationFilter")
public class InitAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final String PASSWORD = "password";
  private static final String USERNAME = "username";

  public InitAuthenticationFilter(
      AuthenticationManager authenticationManager,
      StatelessAuthenticationFailureHandler statelessAuthenticationFailureHandler,
      StatelessAuthenticationSuccessHandler statelessAuthenticationSuccessHandler
  ) {
    super(authenticationManager);
    setAuthenticationFailureHandler(statelessAuthenticationFailureHandler);
    setAuthenticationSuccessHandler(statelessAuthenticationSuccessHandler);
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    return request.getHeader(PASSWORD);
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return request.getHeader(USERNAME);
  }

}

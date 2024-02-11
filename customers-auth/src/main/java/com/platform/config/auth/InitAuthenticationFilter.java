package com.platform.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Initiates a Client authentication.
 */
@Component(value = "initAuthenticationFilter")
public class InitAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

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
    return request.getHeader("password");
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return request.getHeader("username");
  }

}

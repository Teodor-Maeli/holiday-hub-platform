package com.platform.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Initiates a Client authentication.
 */
public class PlatformAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  public PlatformAuthenticationFilter(AuthenticationManager authenticationManager, PlatformAuthenticationFailureHandler platformAuthenticationFailureHandler,
                                      PlatformAuthenticationSuccessHandler platformAuthenticationSuccessHandler) {
    super(authenticationManager);
    setAuthenticationFailureHandler(platformAuthenticationFailureHandler);
    setAuthenticationSuccessHandler(platformAuthenticationSuccessHandler);
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

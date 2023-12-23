package com.platform.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

public class StatefulAuthenticationRedirectStrategy implements RedirectStrategy {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String redirectUrl;

  public StatefulAuthenticationRedirectStrategy(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  @Override
  public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
    logger.debug("Redirecting to {}", redirectUrl);
    response.sendRedirect(redirectUrl);
  }
}

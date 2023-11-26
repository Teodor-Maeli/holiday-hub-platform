package com.platform.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.exception.SessionFailureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles authentication failures. Writes proper output response.
 */
public class StatefulAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  protected StatefulAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException, ServletException {

    if (exception instanceof SessionFailureException e) {

      response.setStatus(e.getHttpStatus().value());

      Map<String, Object> data = new HashMap<>();
      data.put("timestamp", LocalDateTime.now().toString());
      data.put("exception", e.getMessage());

      response.getOutputStream().println(objectMapper.writeValueAsString(data));
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    } else {
      super.onAuthenticationFailure(request, response, exception);
    }

  }
}

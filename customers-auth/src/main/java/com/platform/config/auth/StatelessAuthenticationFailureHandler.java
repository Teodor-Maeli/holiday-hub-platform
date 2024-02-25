package com.platform.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.model.AuthenticationFailure;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class StatelessAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Value("${platform.security.login.failureUrl}")
  private String failureUrl;

  protected StatelessAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException {

    AuthenticationFailure failure = AuthenticationFailure.create()
        .withMessage(exception.getMessage())
        .withTimestamp(LocalDateTime.now().toString());

    response.getOutputStream().println(objectMapper.writeValueAsString(failure));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

}

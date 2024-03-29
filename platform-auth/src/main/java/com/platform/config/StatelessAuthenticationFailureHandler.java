package com.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.annotation.LogAuthentication;
import com.platform.model.AuthenticationFailure;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class StatelessAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  protected StatelessAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  @LogAuthentication(async = true)
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException {

    AuthenticationFailure failure = AuthenticationFailure.create()
        .withMessage(exception.getMessage())
        .withDetails(exception.getClass().getSimpleName())
        .withTimestamp(LocalDateTime.now().toString());

    response.getOutputStream().println(objectMapper.writeValueAsString(failure));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

}

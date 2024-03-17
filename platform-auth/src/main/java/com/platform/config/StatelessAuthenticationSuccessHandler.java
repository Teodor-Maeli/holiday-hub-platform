package com.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.annotation.LogAuthentication;
import com.platform.model.ClientUserDetails;
import com.platform.model.JWTComposite;
import com.platform.util.JWTGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StatelessAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JWTGenerator JWTGenerator;
  private final ObjectMapper objectMapper;

  public StatelessAuthenticationSuccessHandler(
      JWTGenerator JWTGenerator,
      ObjectMapper objectMapper
  ) {
    this.JWTGenerator = JWTGenerator;
    this.objectMapper = objectMapper;
  }

  @Override
  @LogAuthentication(async = true)
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException {
    ClientUserDetails details = (ClientUserDetails) authentication.getPrincipal();
    JWTComposite authTokensComposite = JWTGenerator.generate(details);

    response.getOutputStream().println(objectMapper.writeValueAsString(authTokensComposite));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

}

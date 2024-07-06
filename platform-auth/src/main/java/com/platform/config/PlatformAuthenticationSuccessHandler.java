package com.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.aspect.LogAuthentication;
import com.platform.model.ClientUserDetails;
import com.platform.model.JWTComposite;
import com.platform.component.JWTGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PlatformAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JWTGenerator JWTGenerator;
  private final ObjectMapper objectMapper;

  @Override
  @LogAuthentication(async = true)
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    ClientUserDetails details = (ClientUserDetails) authentication.getPrincipal();
    JWTComposite authTokensComposite = JWTGenerator.generate(details);

    response.getOutputStream().println(objectMapper.writeValueAsString(authTokensComposite));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

}

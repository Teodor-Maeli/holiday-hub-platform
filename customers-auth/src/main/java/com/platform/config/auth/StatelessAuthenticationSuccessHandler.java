package com.platform.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.model.JWTComposite;
import com.platform.config.util.JWTGenerator;
import com.platform.domain.entity.AuthenticationAuditLog;
import com.platform.common.model.AuthenticationStatus;
import com.platform.common.model.AuthenticationStatusReason;
import com.platform.domain.entity.Client;
import com.platform.domain.repository.AuthenticationAuditLogRepository;
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

  private final AuthenticationAuditLogRepository authenticationAuditLogRepository;

  public StatelessAuthenticationSuccessHandler(
      JWTGenerator JWTGenerator,
      ObjectMapper objectMapper,
      AuthenticationAuditLogRepository authenticationAuditLogRepository
  ) {
    this.JWTGenerator = JWTGenerator;
    this.objectMapper = objectMapper;
    this.authenticationAuditLogRepository = authenticationAuditLogRepository;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException {
    Client client = (Client) authentication.getPrincipal();
    JWTComposite authTokensComposite = JWTGenerator.generate(client);
    persistAuthTokenInfo(client);

    response.getOutputStream().println(objectMapper.writeValueAsString(authTokensComposite));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

  private void persistAuthTokenInfo(Client client) {
    AuthenticationAuditLog authenticationAuditLog = new AuthenticationAuditLog();
    authenticationAuditLog.setClient(client);
    authenticationAuditLog.setAuthenticationStatus(AuthenticationStatus.AUTHORIZED);
    authenticationAuditLog.setStatusReason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION);
    authenticationAuditLog.setStatusResolved(Boolean.TRUE);

    authenticationAuditLogRepository.save(authenticationAuditLog);
  }

}

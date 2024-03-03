package com.platform.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.model.ClientUserDetails;
import com.platform.config.model.JWTComposite;
import com.platform.config.util.JWTGenerator;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import com.platform.persistence.repository.AuthenticationLogRepository;
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

  private final AuthenticationLogRepository authenticationAuditLogRepository;

  public StatelessAuthenticationSuccessHandler(
      JWTGenerator JWTGenerator,
      ObjectMapper objectMapper,
      AuthenticationLogRepository authenticationAuditLogRepository
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
    ClientUserDetails details = (ClientUserDetails) authentication.getPrincipal();
    JWTComposite authTokensComposite = JWTGenerator.generate(details);
    persistAuthTokenInfo(details);

    response.getOutputStream().println(objectMapper.writeValueAsString(authTokensComposite));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

  private void persistAuthTokenInfo(ClientUserDetails details) {

    AuthenticationLogEntity authenticationAuditLog = new AuthenticationLogEntity();
    authenticationAuditLog.setClient(details.client());
    authenticationAuditLog.setAuthenticationStatus(AuthenticationStatus.AUTHORIZED);
    authenticationAuditLog.setStatusReason(AuthenticationStatusReason.SUCCESSFUL_AUTHENTICATION);
    authenticationAuditLog.setStatusResolved(Boolean.TRUE);

    authenticationAuditLogRepository.save(authenticationAuditLog);
  }

}

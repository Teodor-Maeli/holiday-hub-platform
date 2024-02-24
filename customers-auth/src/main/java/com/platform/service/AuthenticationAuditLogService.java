package com.platform.service;

import com.platform.domain.entity.AuthenticationAuditLog;
import com.platform.domain.repository.AuthenticationAuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationAuditLogService {

  private final AuthenticationAuditLogRepository repository;

  public AuthenticationAuditLogService(AuthenticationAuditLogRepository repository) {
    this.repository = repository;
  }

  public List<AuthenticationAuditLog> getClientAuthenticationLogs(String username) {
   return repository.findByClientUsername(username);
  }

}

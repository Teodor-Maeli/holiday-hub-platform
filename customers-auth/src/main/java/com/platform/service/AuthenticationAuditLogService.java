package com.platform.service;

import com.platform.persistence.entity.AuthenticationAuditLog;
import com.platform.persistence.repository.AuthenticationAuditLogRepository;
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

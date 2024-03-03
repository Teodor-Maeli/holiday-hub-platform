package com.platform.service;

import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.repository.AuthenticationLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationAuditLogService {

  private final AuthenticationLogRepository repository;

  public AuthenticationAuditLogService(AuthenticationLogRepository repository) {
    this.repository = repository;
  }

  public List<AuthenticationLogEntity> getClientAuthenticationLogs(String username) {
   return repository.findByClientUsername(username);
  }

}

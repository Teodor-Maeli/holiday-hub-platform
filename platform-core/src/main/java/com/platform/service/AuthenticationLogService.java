package com.platform.service;

import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.repository.AuthenticationLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationLogService {

  private final AuthenticationLogRepository repository;

  public AuthenticationLogService(AuthenticationLogRepository repository) {
    this.repository = repository;
  }

  public List<AuthenticationLogEntity> getClientAuthenticationLogs(String username) {
   return repository.findByClientUsername(username);
  }

  public AuthenticationLogEntity logAuthenticationResult(AuthenticationLogEntity entity) {
    return repository.save(entity);
  }

}

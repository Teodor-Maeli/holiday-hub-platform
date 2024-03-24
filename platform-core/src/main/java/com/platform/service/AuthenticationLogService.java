package com.platform.service;

import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.repository.AuthenticationLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationLogService {

  @Value("${platform.security.accounts.auto-locking.bad-credentials.expiry-time}")
  private Integer badCredentialsExpiryTime;

  private final AuthenticationLogRepository repository;

  public AuthenticationLogService(AuthenticationLogRepository repository) {
    this.repository = repository;
  }

  public List<AuthenticationLogEntity> getClientAuthenticationLogs(Long id) {
    return repository.findByClientId(id);
  }

  public void logAuthenticationResult(AuthenticationLogEntity entity) {
    repository.save(entity);
  }

  public List<AuthenticationLogEntity> getBlockingAuthenticationLogs(String username) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime thirtyMinusBeforeNow = LocalDateTime.now().minusMinutes(badCredentialsExpiryTime);

    return repository.findByClientUsernameWhereLogsAutoLock(username, thirtyMinusBeforeNow, now);
  }

}

package com.platform.service;

import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.repository.AuthenticationAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationAttemptService {

  private final AuthenticationAttemptRepository repository;
  @Value("${platform-security.bad-credentials-expiry-time}")
  private Long badCredentialsExpiryTime;

  public List<AuthenticationAttempt> getAuthenticationAttempts(Long id) {
    return repository.findByCustomerId(id);
  }

  public void recordAttempt(AuthenticationAttempt entity) {
    repository.save(entity);
  }

  public List<AuthenticationAttempt> getBlockingAuthenticationAttempts(String username) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime thirtyMinusBeforeNow = LocalDateTime.now().minusMinutes(badCredentialsExpiryTime);

    return repository.finAutoLockedByUsername(username, thirtyMinusBeforeNow, now);
  }

}

package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.mapper.AuthenticationAttemptMapper;
import com.platform.model.AuthenticationAttemptResource;
import com.platform.model.CustomerResource;
import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.repository.AuthenticationAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class AuthenticationAttemptService {

  private final AuthenticationAttemptRepository repository;
  private final AuthenticationAttemptMapper mapper;

  @Value("${platform-security.bad-credentials-expiry-time}")
  private Long badCredentialsExpiryTime;

  private final BiFunction<String, CustomerService, Optional<CustomerResource>> findCustomerByUsername =
      (username, service) -> Optional.of(service.retrieve(username));

  public List<AuthenticationAttemptResource> getAuthenticationAttempts(Long id) {
    List<AuthenticationAttempt> attempts = repository.findByCustomerId(id);
    return mapper.toResource(attempts);
  }

  public AuthenticationAttemptResource recordAttempt(AuthenticationAttemptResource resource, String username) {
    AuthenticationAttempt entity = mapper.toEntity(resource);
    return mapper.toResource(repository.save(entity));
  }

  public List<AuthenticationAttemptResource> getBlockingAuthenticationAttempts(String username) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime thirtyMinusBeforeNow = LocalDateTime.now().minusMinutes(badCredentialsExpiryTime);

    List<AuthenticationAttempt> attempts = repository.finAutoLockedByUsername(username, thirtyMinusBeforeNow, now);

    return mapper.toResource(attempts);
  }

}

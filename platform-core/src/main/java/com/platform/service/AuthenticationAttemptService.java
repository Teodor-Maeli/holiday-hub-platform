package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.mapper.AuthenticationAttemptMapper;
import com.platform.mapper.CompanyMapper;
import com.platform.mapper.PersonMapper;
import com.platform.model.AuthenticationAttemptResource;
import com.platform.model.CompanyResource;
import com.platform.model.CustomerResource;
import com.platform.model.CustomerType;
import com.platform.model.PersonResource;
import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.entity.Customer;
import com.platform.persistence.repository.AuthenticationAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationAttemptService {

  private final AuthenticationAttemptRepository repository;
  private final AuthenticationAttemptMapper mapper;
  private final PersonMapper personMapper;
  private final CompanyMapper companyMapper;

  @Value("${platform-security.bad-credentials-expiry-time}")
  private Long badCredentialsExpiryTime;

  public List<AuthenticationAttemptResource> getAuthenticationAttempts(Long id) {
    List<AuthenticationAttempt> attempts = repository.findByCustomerId(id);
    return mapper.toResource(attempts);
  }

  public AuthenticationAttemptResource recordAttempt(AuthenticationAttemptResource attemptResource, CustomerResource customerResource) {
    Customer customer = toCustomer(customerResource);

    AuthenticationAttempt entity = mapper.toEntity(attemptResource);
    entity.setCustomer(customer);

    return mapper.toResource(repository.save(entity));
  }

  private Customer toCustomer(CustomerResource customerResource) {
    CustomerType type = CustomerType.resolve(customerResource);

    return switch (type) {
      case PERSON -> personMapper.toEntityUpdate((PersonResource) customerResource);
      case COMPANY -> companyMapper.toEntityUpdate((CompanyResource) customerResource);
      case null, default -> throw new PlatformBackendException()
          .setDetails("Unable to remap customerResource to customer during persisting of authentication attempt!")
          .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    };
  }

  public List<AuthenticationAttemptResource> getBlockingAuthenticationAttempts(String username) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime thirtyMinusBeforeNow = LocalDateTime.now().minusMinutes(badCredentialsExpiryTime);

    List<AuthenticationAttempt> attempts = repository.finAutoLockedByUsername(username, thirtyMinusBeforeNow, now);

    return mapper.toResource(attempts);
  }

}

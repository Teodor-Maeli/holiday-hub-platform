package com.platform.service;

import com.platform.domain.entity.Person;
import com.platform.domain.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends AbstractClientService<Person, Long, PersonRepository> {

  protected PersonService(
      PersonRepository repository,
      PasswordEncoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationAuditLogService authenticationAuditLogService
  ) {
    super(repository, encoder, subscriptionService, authenticationAuditLogService);
  }

}

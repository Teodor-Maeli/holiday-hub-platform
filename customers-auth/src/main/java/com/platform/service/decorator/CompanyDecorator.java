package com.platform.service.decorator;

import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.AuthenticationAuditLogService;
import com.platform.service.SubscriptionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CompanyDecorator extends ClientServiceDecorator<Company, Long, CompanyRepository> {

  private final PersonRepository personRepository;

  protected CompanyDecorator(
      CompanyRepository repository,
      PasswordEncoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationAuditLogService authenticationAuditLogService,
      PersonRepository personRepository) {
    super(repository, encoder, subscriptionService, authenticationAuditLogService);
    this.personRepository = personRepository;
  }

  @Override
  void decorateWithClients(Company clientEntity) {
    Set<Person> representatives = personRepository.findByCompanyUsername(clientEntity.getUsername());
    clientEntity.setRepresentatives(representatives);
  }

}

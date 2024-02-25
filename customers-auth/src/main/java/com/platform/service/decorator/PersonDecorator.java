package com.platform.service.decorator;

import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.AuthenticationAuditLogService;
import com.platform.service.SubscriptionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PersonDecorator extends ClientServiceDecorator<Person, Long, PersonRepository> {

  private final CompanyRepository companyRepository;

  protected PersonDecorator(
      PersonRepository repository,
      PasswordEncoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationAuditLogService authenticationAuditLogService,
      CompanyRepository companyRepository) {
    super(repository, encoder, subscriptionService, authenticationAuditLogService);
    this.companyRepository = companyRepository;
  }

  @Override
  void decorateWithClients(Person clientEntity) {
    Company company = companyRepository.findByRepresentativesUsername(clientEntity.getUsername());
    clientEntity.setCompany(company);
  }

}

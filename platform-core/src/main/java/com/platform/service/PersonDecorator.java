package com.platform.service;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import org.springframework.stereotype.Component;

@Component("personService")
public class PersonDecorator extends ClientServiceDecorator<PersonEntity> {

  private final CompanyRepository companyRepository;

  protected PersonDecorator(
      SubscriptionService subscriptionService,
      AuthenticationLogService authenticationLogService,
      CompanyRepository companyRepository,
      PersonRepository personRepository,
      Encoder encoder) {
    super(subscriptionService, authenticationLogService, new PersonService(personRepository, encoder));
    this.companyRepository = companyRepository;
  }

  @Override
  void decorateWithClients(PersonEntity clientEntity) {
    CompanyEntity company = companyRepository.findByRepresentativesUsername(clientEntity.getUsername());
    clientEntity.setCompany(company);
  }
}

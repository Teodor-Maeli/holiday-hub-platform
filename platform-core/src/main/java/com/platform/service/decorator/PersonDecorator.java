package com.platform.service.decorator;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.AuthenticationLogService;
import com.platform.service.Encoder;
import com.platform.service.PersonService;
import com.platform.service.SubscriptionService;
import org.springframework.stereotype.Component;

@Component("personService")
public class PersonDecorator extends ClientServiceDecorator<PersonEntity, PersonService> {

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

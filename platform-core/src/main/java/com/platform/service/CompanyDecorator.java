package com.platform.service;

import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("companyService")
public class CompanyDecorator extends CustomerDecorator<Company> {

  private final PersonRepository personRepository;

  protected CompanyDecorator(SubscriptionService subscriptionService,
      AuthenticationAttemptService authenticationAttemptService,
      PersonRepository personRepository,
      CompanyRepository companyRepository,
      Encoder encoder) {
    super(subscriptionService, authenticationAttemptService, new CompanyService(companyRepository, encoder));
    this.personRepository = personRepository;
  }

  @Override
  void decorateWithCustomers(Company clientEntity) {
    Set<Person> representatives = personRepository.findByCompanyUsername(clientEntity.getUsername());
    clientEntity.setRepresentatives(representatives);
  }

}

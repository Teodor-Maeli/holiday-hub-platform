package com.platform.service;

import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import org.springframework.stereotype.Component;

@Component("personService")
public class PersonDecorator extends CustomerDecorator<Person> {

  private final CompanyRepository companyRepository;

  protected PersonDecorator(SubscriptionService subscriptionService,
      AuthenticationAttemptService authenticationAttemptService,
      CompanyRepository companyRepository,
      PersonRepository personRepository,
      Encoder encoder) {
    super(subscriptionService, authenticationAttemptService, new PersonService(personRepository, encoder));
    this.companyRepository = companyRepository;
  }

  @Override
  void decorateWithCustomers(Person clientEntity) {
    Company company = companyRepository.findByRepresentativesUsername(clientEntity.getUsername());
    clientEntity.setCompany(company);
  }
}

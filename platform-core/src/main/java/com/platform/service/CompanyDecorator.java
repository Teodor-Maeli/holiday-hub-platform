package com.platform.service;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("companyService")
public class CompanyDecorator extends ClientServiceDecorator<CompanyEntity> {

  private final PersonRepository personRepository;

  protected CompanyDecorator(
      SubscriptionService subscriptionService,
      AuthenticationLogService authenticationLogService,
      PersonRepository personRepository,
      CompanyRepository companyRepository,
      Encoder encoder) {
    super(subscriptionService, authenticationLogService, new CompanyService(companyRepository, encoder));
    this.personRepository = personRepository;
  }

  @Override
  void decorateWithCustomers(CompanyEntity clientEntity) {
    Set<PersonEntity> representatives = personRepository.findByCompanyUsername(clientEntity.getUsername());
    clientEntity.setRepresentatives(representatives);
  }

}

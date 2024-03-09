package com.platform.service.decorator;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.AuthenticationLogService;
import com.platform.service.Encoder;
import com.platform.service.SubscriptionService;
import org.springframework.stereotype.Component;

@Component
public class PersonDecorator extends ClientServiceDecorator<PersonEntity, Long, PersonRepository> {

  private final CompanyRepository companyRepository;

  protected PersonDecorator(
      PersonRepository repository,
      Encoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationLogService authenticationLogService,
      CompanyRepository companyRepository) {
    super(repository, encoder, subscriptionService, authenticationLogService);
    this.companyRepository = companyRepository;
  }

  @Override
  void decorateWithClients(PersonEntity clientEntity) {
    CompanyEntity company = companyRepository.findByRepresentativesUsername(clientEntity.getUsername());
    clientEntity.setCompany(company);
  }

}

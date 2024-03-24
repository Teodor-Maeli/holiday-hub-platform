package com.platform.service.decorator;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.AuthenticationLogService;
import com.platform.service.CompanyService;
import com.platform.service.Encoder;
import com.platform.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("companyService")
public class CompanyDecorator extends ClientServiceDecorator<CompanyEntity, CompanyService> {

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
  void decorateWithClients(CompanyEntity clientEntity) {
    Set<PersonEntity> representatives = personRepository.findByCompanyUsername(clientEntity.getUsername());
    clientEntity.setRepresentatives(representatives);
  }

}

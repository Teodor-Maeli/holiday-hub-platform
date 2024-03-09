package com.platform.service.decorator;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.AuthenticationLogService;
import com.platform.service.Encoder;
import com.platform.service.SubscriptionService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CompanyDecorator extends ClientServiceDecorator<CompanyEntity, Long, CompanyRepository> {

  private final PersonRepository personRepository;

  protected CompanyDecorator(
      CompanyRepository repository,
      Encoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationLogService authenticationLogService,
      PersonRepository personRepository) {
    super(repository, encoder, subscriptionService, authenticationLogService);
    this.personRepository = personRepository;
  }

  @Override
  void decorateWithClients(CompanyEntity clientEntity) {
    Set<PersonEntity> representatives = personRepository.findByCompanyUsername(clientEntity.getUsername());
    clientEntity.setRepresentatives(representatives);
  }

}

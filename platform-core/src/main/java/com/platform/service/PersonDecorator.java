package com.platform.service;

import com.platform.mapper.PersonMapper;
import com.platform.model.CustomerType;
import com.platform.persistence.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonDecorator extends CustomerDecorator {

  protected PersonDecorator(SubscriptionService subscriptionService,
                            AuthenticationAttemptService authenticationAttemptService,
                            PersonRepository personRepository,
                            PersonMapper mapper) {
    super(subscriptionService, authenticationAttemptService, new PersonService(personRepository, mapper));
  }

  @Override
  public CustomerType serviceType() {
    return CustomerType.PERSON;
  }
}

package com.platform.service;

import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.decorator.DecoratingOptions;

import java.util.Set;

public class PersonService extends AbstractClientService<PersonEntity, Long, PersonRepository> {

  public PersonService(
      PersonRepository personRepository,
      Encoder encoder) {
    super(personRepository, encoder);
  }

  @Override
  public PersonEntity loadUserByUsernameDecorated(Set<DecoratingOptions> decoratingOptions, String username) {
    return super.loadUserByUsername(username);
  }
}

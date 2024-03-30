package com.platform.service;

import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.PersonRepository;

class PersonService extends AbstractClientService<PersonEntity, Long, PersonRepository> {

  public PersonService(
      PersonRepository personRepository,
      Encoder encoder) {
    super(personRepository, encoder);
  }
}

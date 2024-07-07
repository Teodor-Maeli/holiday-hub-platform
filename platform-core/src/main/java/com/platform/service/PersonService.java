package com.platform.service;

import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.PersonRepository;

class PersonService extends AbstractCustomerService<Person, Long, PersonRepository> {

  public PersonService(PersonRepository personRepository, Encoder encoder) {
    super(personRepository, encoder);
  }
}

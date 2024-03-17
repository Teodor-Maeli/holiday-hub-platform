package com.platform.service;

import com.platform.persistence.entity.PersonEntity;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.decorator.DecoratingOptions;
import com.platform.service.decorator.PersonDecorator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService extends AbstractClientService<PersonEntity, Long, PersonRepository> {

  private final PersonDecorator decorator;

  protected PersonService(
      PersonRepository personRepository,
      Encoder encoder,
      PersonDecorator decorator) {
    super(personRepository, encoder);
    this.decorator = decorator;
  }

  @Override
  public PersonEntity loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    return decorator.loadUserByUsername(decoratingOptions, username);
  }

}

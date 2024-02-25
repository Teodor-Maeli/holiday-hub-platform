package com.platform.service;

import com.platform.common.model.DecoratingOptions;
import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.PersonRepository;
import com.platform.service.decorator.PersonDecorator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService extends AbstractClientService<Person, Long, PersonRepository> {

  private final PersonDecorator decorator;

  protected PersonService(
      PersonRepository personRepository,
      PasswordEncoder encoder,
      PersonDecorator decorator) {
    super(personRepository, encoder);
    this.decorator = decorator;
  }

  @Override
  public Person loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    return decorator.loadUserByUsername(decoratingOptions, username);
  }

}

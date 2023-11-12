package com.platform.rest.assemblers;

import com.platform.domain.entity.Person;
import com.platform.models.PersonRequest;
import com.platform.models.PersonResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler extends AbstractClientAssembler<PersonResponse, PersonRequest, Person> {

  protected PersonAssembler(PasswordEncoder encoder) {
    super(encoder);
  }

  @Override
  protected PersonResponse initResponse() {
    return new PersonResponse();
  }

  @Override
  protected Person initEntity() {
    return new Person();
  }
}

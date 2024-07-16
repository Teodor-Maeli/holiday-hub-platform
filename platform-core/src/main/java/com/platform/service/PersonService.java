package com.platform.service;

import com.platform.mapper.PersonMapper;
import com.platform.model.CustomerResource;
import com.platform.model.CustomerType;
import com.platform.model.PersonResource;
import com.platform.persistence.entity.Person;
import com.platform.persistence.repository.PersonRepository;

class PersonService implements CustomerService {

  private final CustomerHelperService<Person, Long> helperService;
  private final PersonMapper mapper;

  public PersonService(PersonRepository repository, PersonMapper mapper) {
    this.helperService = new CustomerHelperService<>(repository);
    this.mapper = mapper;
  }

  @Override
  public CustomerResource retrieve(String username) {
    Person person = helperService.retrieve(username);
    return mapper.toResource(person);
  }

  @Override
  public CustomerResource create(CustomerResource resource) {
    Person entity = mapper.toEntityCreate((PersonResource) resource);
    return mapper.toResource(helperService.create(entity));
  }

  @Override
  public CustomerResource update(CustomerResource resource) {
    Person entity = mapper.toEntityUpdate((PersonResource) resource);
    return mapper.toResource(helperService.create(entity));
  }

  @Override
  public CustomerType serviceType() {
    return CustomerType.IGNORE;
  }
}

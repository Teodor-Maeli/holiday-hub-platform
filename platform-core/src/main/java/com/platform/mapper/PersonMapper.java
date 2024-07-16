package com.platform.mapper;

import com.platform.model.PersonResource;
import com.platform.persistence.entity.Person;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class,
        AuthenticationAttemptMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PersonMapper {

  @Mapping(target = "type", constant = "PERSON")
  PersonResource toResource(Person entity);

  @Mapping(target = "password", source = "password", qualifiedBy = EncodedMapping.class)
  Person toEntityCreate(PersonResource request);

  @Named(value = "toEntityUpdate")
  Person toEntityUpdate(PersonResource request);

  @AfterMapping
  default void afterMapping(@MappingTarget Person person) {
      person.getConfiguration().setCustomer(person);
  }
}

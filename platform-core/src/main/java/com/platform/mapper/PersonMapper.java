package com.platform.mapper;

import com.platform.model.PersonResponse;
import com.platform.model.registration.PersonRegistrationRequest;
import com.platform.persistence.entity.Person;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PersonMapper {

  PersonResponse toResponse(Person entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  Person toEntity(PersonRegistrationRequest request);

}

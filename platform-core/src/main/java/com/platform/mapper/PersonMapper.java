package com.platform.mapper;

import com.platform.model.PersonResource;
import com.platform.persistence.entity.Person;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

  @Mapping(target = "password", ignore = true)
  PersonResource toResource(Person entity);

  @Mapping(target = "password", source = "password", qualifiedBy = EncodedMapping.class)
  Person toEntity(PersonResource request);
}

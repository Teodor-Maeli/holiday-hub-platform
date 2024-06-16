package com.platform.mapper;

import com.platform.model.Person;
import com.platform.model.registration.PersonRegistration;
import com.platform.persistence.entity.PersonEntity;
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

  Person toResponse(PersonEntity entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  PersonEntity toEntity(PersonRegistration request);

}

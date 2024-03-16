package com.platform.mapper;

import com.platform.persistence.entity.PersonEntity;
import com.platform.model.PersonRegistration;
import com.platform.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    }
)
public interface PersonMapper {

  Person toResponse(PersonEntity entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  PersonEntity toEntity(PersonRegistration request);

}

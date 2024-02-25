package com.platform.rest.mapper;

import com.platform.persistence.entity.Person;
import com.platform.rest.resource.PersonRegistration;
import com.platform.rest.resource.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    }
)
public interface PersonMapper extends LazyLoadingAwareMapper {

  PersonResponse toResponse(Person entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  Person toEntity(PersonRegistration request);

}

package com.platform.rest.mapper;

import com.platform.domain.entity.Person;
import com.platform.rest.resource.PersonRequest;
import com.platform.rest.resource.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SessionMapper.class,
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    }
)
public interface PersonMapper {

  PersonResponse toResponse(Person entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  Person toEntity(PersonRequest request);
}

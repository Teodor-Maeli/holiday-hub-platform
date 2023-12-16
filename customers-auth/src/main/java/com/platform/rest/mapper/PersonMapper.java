package com.platform.rest.mapper;

import com.platform.domain.entity.Person;
import com.platform.rest.resource.PersonRequest;
import com.platform.rest.resource.PersonResponse;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        SessionMapper.class,
        SubscriptionMapper.class
    }
)
public interface PersonMapper {

  PersonResponse toResponse(Person entity);

  Person toEntity(PersonRequest request);
}

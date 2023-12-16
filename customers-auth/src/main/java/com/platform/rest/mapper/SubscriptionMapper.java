package com.platform.rest.mapper;

import com.platform.domain.entity.Subscription;
import com.platform.rest.resource.SubscriptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  @Mapping(source = "id", target = "clientId")
  SubscriptionResponse toResponse(Subscription entity);
}

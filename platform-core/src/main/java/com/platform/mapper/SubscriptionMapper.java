package com.platform.mapper;

import com.platform.model.SubscriptionResponse;
import com.platform.persistence.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  Set<SubscriptionResponse> toResponse(Set<Subscription> subscriptionsList);

  @Mapping(source = "id", target = "clientId")
  SubscriptionResponse toResponse(Subscription subscription);

}

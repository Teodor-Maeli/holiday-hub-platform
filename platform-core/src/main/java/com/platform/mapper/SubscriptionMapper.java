package com.platform.mapper;

import com.platform.persistence.entity.SubscriptionEntity;
import com.platform.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  Set<Subscription> toResponse(Set<SubscriptionEntity> subscriptionsList);

  @Mapping(source = "id", target = "clientId")
  Subscription toResponse(SubscriptionEntity subscription);

}

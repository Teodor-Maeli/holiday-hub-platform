package com.platform.rest.mapper;

import com.platform.domain.entity.Subscription;
import com.platform.rest.resource.SubscriptionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  List<SubscriptionResponse> toResponse(List<Subscription> subscriptionsList);

  @Mapping(source = "id", target = "clientId")
  SubscriptionResponse toResponse(Subscription subscription);

}

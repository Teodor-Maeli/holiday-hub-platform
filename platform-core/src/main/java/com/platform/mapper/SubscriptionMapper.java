package com.platform.mapper;

import com.platform.model.SubscriptionResource;
import com.platform.persistence.entity.Subscription;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  List<SubscriptionResource> toResource(List<Subscription> subscriptionsList);

  SubscriptionResource toResource(Subscription subscription);

  Subscription toEntity(SubscriptionResource resources);

}

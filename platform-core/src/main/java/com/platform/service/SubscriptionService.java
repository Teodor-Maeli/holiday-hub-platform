package com.platform.service;

import com.platform.mapper.SubscriptionMapper;
import com.platform.model.SubscriptionResource;
import com.platform.persistence.entity.Subscription;
import com.platform.persistence.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository repository;
  private final SubscriptionMapper mapper;

  public List<SubscriptionResource> getSubscriptions(Long customerId) {
    List<Subscription> subscriptions = repository.findByCustomerId(customerId);

    return mapper.toResource(subscriptions);
  }

}

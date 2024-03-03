package com.platform.service;

import com.platform.persistence.entity.SubscriptionEntity;
import com.platform.persistence.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public SubscriptionService(SubscriptionRepository repository) {
    this.repository = repository;
  }

  public List<SubscriptionEntity> getClientSubscriptions(String username) {
    return repository.findByClientUsername(username);
  }

}

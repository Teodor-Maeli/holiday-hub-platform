package com.platform.service;

import com.platform.domain.entity.Subscription;
import com.platform.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public SubscriptionService(SubscriptionRepository repository) {
    this.repository = repository;
  }

  public List<Subscription> getClientSubscriptions(String username) {
    return repository.findByClientUsername(username);
  }

}

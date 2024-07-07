package com.platform.service;

import com.platform.persistence.entity.Subscription;
import com.platform.persistence.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public List<Subscription> getSubscriptions(Long id) {
    return repository.findByClientId(id);
  }

}

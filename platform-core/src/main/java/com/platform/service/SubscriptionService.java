package com.platform.service;

import com.platform.persistence.entity.SubscriptionEntity;
import com.platform.persistence.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public List<SubscriptionEntity> getClientSubscriptions(Long id) {
    return repository.findByClientId(id);
  }

}

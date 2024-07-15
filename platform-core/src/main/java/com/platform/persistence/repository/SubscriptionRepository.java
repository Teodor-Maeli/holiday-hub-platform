package com.platform.persistence.repository;

import com.platform.persistence.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  List<Subscription> findByCustomerId(Long customerId);
}

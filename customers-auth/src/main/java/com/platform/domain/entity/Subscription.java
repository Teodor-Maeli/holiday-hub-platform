package com.platform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription {

  @Id
  @Column(name = "CLIENT_ID", updatable = false, unique = true)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "CLIENT_ID", unique = true)
  private Client client;

  @Column(name = "SUBSCRIPTION_STARTS")
  private LocalDateTime subscriptionStarts;

  @Column(name = "SUBSCRIPTION_ENDS")
  private LocalDateTime subscriptionEnds;

  @Column(name = "ACTIVE_SUBSCRIPTION")
  private Boolean activeSubscription;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public LocalDateTime getSubscriptionStarts() {
    return subscriptionStarts;
  }

  public void setSubscriptionStarts(LocalDateTime subscriptionStarts) {
    this.subscriptionStarts = subscriptionStarts;
  }

  public LocalDateTime getSubscriptionEnds() {
    return subscriptionEnds;
  }

  public void setSubscriptionEnds(LocalDateTime subscriptionEnds) {
    this.subscriptionEnds = subscriptionEnds;
  }

  public Boolean getActiveSubscription() {
    return activeSubscription;
  }

  public void setActiveSubscription(Boolean activeSubscription) {
    this.activeSubscription = activeSubscription;
  }
}

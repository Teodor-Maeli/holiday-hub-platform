package com.platform.rest.resource;

import java.time.LocalDateTime;

public class SubscriptionResponse {

  private Long clientId;
  private LocalDateTime subscriptionStarts;

  private LocalDateTime subscriptionEnds;
  private Boolean activeSubscription;

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
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

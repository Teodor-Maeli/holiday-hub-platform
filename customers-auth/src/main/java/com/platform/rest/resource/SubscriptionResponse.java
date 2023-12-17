package com.platform.rest.resource;

public class SubscriptionResponse {

  private Long clientId;
  private String subscriptionStarts;

  private String subscriptionEnds;
  private Boolean activeSubscription;

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public String getSubscriptionStarts() {
    return subscriptionStarts;
  }

  public void setSubscriptionStarts(String subscriptionStarts) {
    this.subscriptionStarts = subscriptionStarts;
  }

  public String getSubscriptionEnds() {
    return subscriptionEnds;
  }

  public void setSubscriptionEnds(String subscriptionEnds) {
    this.subscriptionEnds = subscriptionEnds;
  }

  public Boolean getActiveSubscription() {
    return activeSubscription;
  }

  public void setActiveSubscription(Boolean activeSubscription) {
    this.activeSubscription = activeSubscription;
  }
}

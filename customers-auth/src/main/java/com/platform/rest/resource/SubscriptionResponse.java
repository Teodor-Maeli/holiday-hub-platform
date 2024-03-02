package com.platform.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.platform.model.SubscriptionType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionResponse {

  private Long clientId;

  private String startDate;

  private String endDate;

  private Boolean activeSubscription;

  private SubscriptionType subscriptionType;

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Boolean getActiveSubscription() {
    return activeSubscription;
  }

  public void setActiveSubscription(Boolean activeSubscription) {
    this.activeSubscription = activeSubscription;
  }

  public SubscriptionType getSubscriptionType() {
    return subscriptionType;
  }

  public void setSubscriptionType(SubscriptionType subscriptionType) {
    this.subscriptionType = subscriptionType;
  }
}

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subscription {

  private Long clientId;

  private String startDate;

  private String endDate;

  private Boolean activeSubscription;

  private SubscriptionType subscriptionType;
}

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SubscriptionResource {

  private LocalDate startDate;
  private LocalDate endDate;
  private Boolean active;
  private SubscriptionType type;
}
package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.platform.persistence.entity.AuthenticationAttempt;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Base response class.
 * Since 1.0
 */

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class CustomerResponse {

  private Long id;
  private String username;
  private String phoneNumber;
  private String emailAddress;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime registeredDate;
  private List<AuthenticationAttempt> authenticationAttempts;
  private List<SubscriptionResponse> subscriptions;
  private Set<ConsumerAuthority> authorities;
  private Boolean accountLocked;
  private String redirectUrl;
  private String returnUrl;
}

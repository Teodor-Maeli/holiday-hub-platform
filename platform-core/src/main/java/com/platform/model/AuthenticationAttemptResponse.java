package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationAttemptResponse {

  private Long id;
  private CustomerResponse customer;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private AuthenticationStatus authenticationStatus;
  private AuthenticationStatusReason statusReason;
  private String message;
  private Boolean statusResolved;
}

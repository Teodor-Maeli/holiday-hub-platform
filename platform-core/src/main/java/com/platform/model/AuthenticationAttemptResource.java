package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationAttemptResource {

  private Long id;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private AuthenticationStatus authenticationStatus;
  private AuthenticationStatusReason statusReason;
  private String message;
  private Boolean statusResolved;
  private String updatedBy;
  private String encodedUnlockingCode;
}

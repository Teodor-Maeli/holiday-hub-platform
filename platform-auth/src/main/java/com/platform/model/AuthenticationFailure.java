package com.platform.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationFailure {

  private String timestamp;
  private String message;
  private String details;

  public static AuthenticationFailure create() {
    return new AuthenticationFailure();
  }
}

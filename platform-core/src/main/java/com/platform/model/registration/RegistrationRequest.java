package com.platform.model.registration;

import lombok.Getter;
import lombok.Setter;

/**
 * Base registration request.
 * Since 1.0
 */
@Getter
@Setter
public class RegistrationRequest {

  private String username;
  private String password;
  private String emailAddress;
  private String phoneNumber;
}

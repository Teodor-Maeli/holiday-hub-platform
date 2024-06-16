package com.platform.model.registration;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonRegistration extends RegistrationRequest {

  private String familyName;
  private String givenName;
  private String middleName;
  private LocalDate birthDate;
  private Long companyConfigurationId;
}

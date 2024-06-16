package com.platform.model.registration;

import com.platform.persistence.entity.ConfigurationEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistration extends RegistrationRequest {

  private String companyName;
  private String companyNumber;
  private ConfigurationEntity configuration;
}

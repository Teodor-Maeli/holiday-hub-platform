package com.platform.model.registration;

import com.platform.persistence.entity.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegisterRequest extends RegistrationRequest {

  private String companyName;
  private String companyNumber;
  private Configuration configuration;
}

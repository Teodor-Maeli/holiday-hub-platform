package com.platform.rest.resource;

public class CompanyRegistration extends RegistrationRequest {

  private String companyName;

  private String companyNumber;

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyNumber() {
    return companyNumber;
  }

  public void setCompanyNumber(String companyNumber) {
    this.companyNumber = companyNumber;
  }
}

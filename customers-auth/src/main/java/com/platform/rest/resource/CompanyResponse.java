package com.platform.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Set;

@JsonInclude(Include.NON_NULL)
public class CompanyResponse extends ClientResponse {

  private String companyName;

  private String companyNumber;

  private Set<PersonResponse> representatives;

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

  public Set<PersonResponse> getRepresentatives() {
    return representatives;
  }

  public void setRepresentatives(Set<PersonResponse> representatives) {
    this.representatives = representatives;
  }
}

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Set;

@JsonInclude(Include.NON_NULL)
public class Company extends Client {

  private String companyName;

  private String companyNumber;

  private Set<Person> representatives;

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

  public Set<Person> getRepresentatives() {
    return representatives;
  }

  public void setRepresentatives(Set<Person> representatives) {
    this.representatives = representatives;
  }
}

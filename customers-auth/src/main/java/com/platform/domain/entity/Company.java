package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMPANY")
public class Company extends Client {

  @Column(name = "COMPANY_NAME", nullable = false)
  private String companyName;

  @Column(name = "COMPANY_NUMBER", nullable = false)
  private String companyNumber;

  @Column(name = "CONTACT_PERSON", nullable = false)
  private String contactPerson;

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

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }
}

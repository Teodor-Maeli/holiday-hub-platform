package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "COMPANY")
public class Company extends Client {

  @Column(name = "COMPANY_NAME", nullable = false)
  private String companyName;

  @Column(name = "COMPANY_NUMBER", nullable = false)
  private String companyNumber;

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  @JsonManagedReference
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

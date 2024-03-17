package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "COMPANY")
public class CompanyEntity extends ClientEntity {

  @Column(name = "COMPANY_NAME", nullable = false)
  private String companyName;

  @Column(name = "COMPANY_NUMBER", nullable = false)
  private String companyNumber;

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<PersonEntity> representatives;

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

  public Set<PersonEntity> getRepresentatives() {
    if (Hibernate.isInitialized(representatives)) {
      return representatives;
    }

    return Collections.emptySet();
  }

  public void setRepresentatives(Set<PersonEntity> representatives) {
    this.representatives = representatives;
  }

}

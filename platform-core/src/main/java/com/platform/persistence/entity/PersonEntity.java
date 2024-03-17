package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "PERSON")
public class PersonEntity extends ClientEntity {

  @Column(name = "GIVEN_NAME", nullable = false)
  private String givenName;

  @Column(name = "FAMILY_NAME", nullable = false)
  private String familyName;

  @Column(name = "MIDDLE_NAME", nullable = false)
  private String middleName;

  @Column(name = "BIRTH_DATE", nullable = false)
  private LocalDate birthDate;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "COMPANY_ID")
  @JsonBackReference
  private CompanyEntity company;

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public CompanyEntity getCompany() {
    return company;
  }

  public void setCompany(CompanyEntity company) {
    this.company = company;
  }
}

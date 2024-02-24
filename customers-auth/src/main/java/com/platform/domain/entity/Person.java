package com.platform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "PERSON")
public class Person extends Client {

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
  private Company company;

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
}

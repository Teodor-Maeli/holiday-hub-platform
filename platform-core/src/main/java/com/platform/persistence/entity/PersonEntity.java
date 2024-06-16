package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

  @Column(name = "COMPANY_CONFIGURATION_ID")
  private Long companyConfigurationId;

}

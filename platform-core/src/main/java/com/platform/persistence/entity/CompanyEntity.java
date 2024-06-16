package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
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
}

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CompanyResponse extends CustomerResponse {

  private String companyName;
  private String companyNumber;
  private Set<PersonResponse> representatives;
}


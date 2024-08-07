package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PersonResource extends CustomerResource {

  private String familyName;
  private String givenName;
  private String middleName;
  private LocalDate birthDate;
  private CompanyResource company;
}

package com.platform.model;

import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;

public enum CustomerType {

  COMPANY,
  PERSON,
  IGNORE;

  public static CustomerType resolve(Object object) {
    return switch (object) {
      case Person person -> CustomerType.PERSON;
      case PersonResource personResource -> CustomerType.PERSON;
      case Company company -> CustomerType.COMPANY;
      case CompanyResource companyResource -> CustomerType.COMPANY;
      case null, default -> null;
    };
  }
}

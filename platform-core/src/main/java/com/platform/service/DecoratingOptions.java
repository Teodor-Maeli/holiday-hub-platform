package com.platform.service;

import com.platform.persistence.entity.Customer;
import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;


/**
 * Client decorating options class, provides basic configuration for entities and their eligibility in regard to decoration.
 */
@Getter
@RequiredArgsConstructor
public enum DecoratingOptions {

  SUBSCRIPTIONS(Set.of(Person.class, Company.class)),
  AUTHENTICATION_ATTEMPTS(Set.of(Person.class, Company.class)),
  BLOCKING_AUTHENTICATION_ATTEMPTS(Set.of(Person.class, Company.class)),
  COMPANY_REPRESENTATIVES(Set.of(Company.class)),
  REPRESENTATIVE_COMPANY(Set.of(Person.class));

  private final Set<Class<? extends Customer>> eligibleForDecorating;
}

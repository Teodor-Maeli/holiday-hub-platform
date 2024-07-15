package com.platform.service;

import com.platform.model.CompanyResource;
import com.platform.model.CustomerResource;
import com.platform.model.PersonResource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;


/**
 * Client decorating options class, provides basic configuration for entities and their eligibility in regard to decoration.
 */
@Getter
@RequiredArgsConstructor
public enum DecoratingOptions {

  SUBSCRIPTIONS(Set.of(PersonResource.class, CompanyResource.class)),
  AUTHENTICATION_ATTEMPTS(Set.of(PersonResource.class, CompanyResource.class)),
  BLOCKING_AUTHENTICATION_ATTEMPTS(Set.of(PersonResource.class, CompanyResource.class)),
  COMPANY_REPRESENTATIVES(Set.of(CompanyResource.class)),
  REPRESENTATIVE_COMPANY(Set.of(PersonResource.class));

  private final Set<Class<? extends CustomerResource>> eligibleForDecorating;
}

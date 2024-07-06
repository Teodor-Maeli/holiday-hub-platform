package com.platform.service;

import com.platform.persistence.entity.CustomerEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;


/**
 * Client decorating options class, provides basic configuration for entities and their eligibility in regard to decoration.
 */
@Getter
@RequiredArgsConstructor
public enum DecoratingOptions {

  SUBSCRIPTIONS(Set.of(PersonEntity.class, CompanyEntity.class)),
  AUTHENTICATION_LOGS(Set.of(PersonEntity.class, CompanyEntity.class)),
  BLOCKING_AUTHENTICATION_LOGS(Set.of(PersonEntity.class, CompanyEntity.class)),
  COMPANY_REPRESENTATIVES(Set.of(CompanyEntity.class)),
  REPRESENTATIVE_COMPANY(Set.of(PersonEntity.class));

  private final Set<Class<? extends CustomerEntity>> eligibleForDecorating;
}

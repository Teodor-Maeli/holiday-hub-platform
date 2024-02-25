package com.platform.common.model;

import com.platform.persistence.entity.Client;
import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Person;

import java.util.Set;


/**
 * Client decorating options class, provides basic configuration for entities and their eligibility in regard to decoration.
 */
public enum DecoratingOptions {
  SUBSCRIPTIONS(Set.of(Person.class, Company.class)),
  AUTHENTICATION_AUDIT_LOGS(Set.of(Person.class, Company.class)),
  COMPANY_REPRESENTATIVES(Set.of(Company.class)),
  REPRESENTATIVE_COMPANY(Set.of(Person.class));

  private final Set<Class<? extends Client>> eligibleForDecorating;

  DecoratingOptions(Set<Class<? extends Client>> eligibleForDecorating) {
    this.eligibleForDecorating = eligibleForDecorating;
  }

  public boolean allowedForClient(Client client) {
    return this.eligibleForDecorating.contains(client.getClass());
  }

}

package com.platform.service.decorator;

import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;

import java.util.Set;


/**
 * Client decorating options class, provides basic configuration for entities and their eligibility in regard to decoration.
 */
public enum DecoratingOptions {

  SUBSCRIPTIONS(Set.of(PersonEntity.class, CompanyEntity.class)),
  AUTHENTICATION_AUDIT_LOGS(Set.of(PersonEntity.class, CompanyEntity.class)),
  COMPANY_REPRESENTATIVES(Set.of(CompanyEntity.class)),
  REPRESENTATIVE_COMPANY(Set.of(PersonEntity.class));

  private final Set<Class<? extends ClientEntity>> eligibleForDecorating;

  DecoratingOptions(Set<Class<? extends ClientEntity>> eligibleForDecorating) {
    this.eligibleForDecorating = eligibleForDecorating;
  }

  public boolean allowedForClient(ClientEntity client) {
    return this.eligibleForDecorating.contains(client.getClass());
  }

}

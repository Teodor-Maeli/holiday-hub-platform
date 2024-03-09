package com.platform.service.decorator;

import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.SubscriptionEntity;
import com.platform.persistence.repository.BaseClientRepository;
import com.platform.service.AbstractClientService;
import com.platform.service.AuthenticationLogService;
import com.platform.service.Encoder;
import com.platform.service.SubscriptionService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base decorator class that utilize the decorator pattern.
 *
 * @param <E>  Entity
 * @param <ID> ID of entity
 * @param <R>  Repository
 */
public abstract class ClientServiceDecorator<E extends ClientEntity, ID extends Number, R extends BaseClientRepository<E, ID>>
    extends AbstractClientService<E, ID, R> {

  private final SubscriptionService subscriptionService;

  private final AuthenticationLogService authenticationLogService;

  protected ClientServiceDecorator(
      R repository,
      Encoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationLogService authenticationLogService
  ) {
    super(repository, encoder);
    this.subscriptionService = subscriptionService;
    this.authenticationLogService = authenticationLogService;
  }

  @Override
  public E loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    E client = super.loadUserByUsername(decoratingOptions, username);

    if (shouldDecorate(decoratingOptions, client)) {
      decoratingOptions = filterDecoratingOptions(decoratingOptions, client);
      decorate(decoratingOptions, client);
    }

    return client;
  }

  private void decorate(Set<DecoratingOptions> decoratingOptions, E clientEntity) {
    for (DecoratingOptions option : decoratingOptions) {
      switch (option) {
        case SUBSCRIPTIONS -> decorateWithSubscriptions(clientEntity);
        case AUTHENTICATION_AUDIT_LOGS -> decorateWithAuthenticationLogs(clientEntity);
        case COMPANY_REPRESENTATIVES, REPRESENTATIVE_COMPANY -> decorateWithClients(clientEntity);
      }
    }

  }

  abstract void decorateWithClients(E clientEntity);

  private void decorateWithAuthenticationLogs(E clientEntity) {
    List<AuthenticationLogEntity> clientAuthenticationLogs = authenticationLogService.getClientAuthenticationLogs(clientEntity.getId());
    clientEntity.setAuthenticationAuditLogs(clientAuthenticationLogs);
  }

  private void decorateWithSubscriptions(E clientEntity) {
    List<SubscriptionEntity> clientSubscriptions = subscriptionService.getClientSubscriptions(clientEntity.getId());
    clientEntity.setSubscriptions(clientSubscriptions);
  }

  private Set<DecoratingOptions> filterDecoratingOptions(Set<DecoratingOptions> decoratingOptions, E client) {
    return decoratingOptions.stream()
        .filter(option -> option.allowedForClient(client))
        .collect(Collectors.toSet());
  }

  private boolean shouldDecorate(Set<DecoratingOptions> options, E client) {
    return client != null && (options != null && ! options.isEmpty());
  }

}

package com.platform.service.decorator;

import com.platform.common.model.DecoratingOptions;
import com.platform.persistence.entity.AuthenticationAuditLog;
import com.platform.persistence.entity.Client;
import com.platform.persistence.entity.Subscription;
import com.platform.persistence.repository.BaseClientRepository;
import com.platform.service.AbstractClientService;
import com.platform.service.AuthenticationAuditLogService;
import com.platform.service.SubscriptionService;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public abstract class ClientServiceDecorator<E extends Client, ID, R extends BaseClientRepository<E, ID>>
    extends AbstractClientService<E, ID, R> {

  private final SubscriptionService subscriptionService;

  private final AuthenticationAuditLogService authenticationAuditLogService;

  protected ClientServiceDecorator(
      R repository,
      PasswordEncoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationAuditLogService authenticationAuditLogService
  ) {
    super(repository, encoder);
    this.subscriptionService = subscriptionService;
    this.authenticationAuditLogService = authenticationAuditLogService;
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

  private E decorate(Set<DecoratingOptions> decoratingOptions, E clientEntity) {
    for (DecoratingOptions option : decoratingOptions) {
      switch (option) {
        case SUBSCRIPTIONS -> decorateWithSubscriptions(clientEntity);
        case AUTHENTICATION_AUDIT_LOGS -> decorateWithAuthenticationLogs(clientEntity);
        case COMPANY_REPRESENTATIVES, REPRESENTATIVE_COMPANY -> decorateWithClients(clientEntity);
      }
    }

    return clientEntity;
  }

  abstract void decorateWithClients(E clientEntity);

  private void decorateWithAuthenticationLogs(E clientEntity) {
    List<AuthenticationAuditLog> clientAuthenticationLogs = authenticationAuditLogService.getClientAuthenticationLogs(clientEntity.getUsername());
    clientEntity.setAuthenticationAuditLogs(clientAuthenticationLogs);
  }

  private void decorateWithSubscriptions(E clientEntity) {
    List<Subscription> clientSubscriptions = subscriptionService.getClientSubscriptions(clientEntity.getUsername());
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

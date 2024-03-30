package com.platform.service;

import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.SubscriptionEntity;
import com.platform.util.ObjectsHelper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base decorator class that utilize the decorator pattern.
 *
 * @param <E> Entity
 */
public abstract class ClientServiceDecorator<E extends ClientEntity> implements ClientService<E> {

  private final SubscriptionService subscriptionService;

  private final AuthenticationLogService authenticationLogService;

  private final ClientService<E> delegate;


  protected ClientServiceDecorator(
      SubscriptionService subscriptionService,
      AuthenticationLogService authenticationLogService,
      ClientService<E> delegate) {
    this.subscriptionService = subscriptionService;
    this.authenticationLogService = authenticationLogService;
    this.delegate = delegate;
  }

  @Override
  public E loadUserByUsernameDecorated(Set<DecoratingOptions> decoratingOptions, String username) {
    E client = delegate.loadUserByUsername(username);

    if (shouldDecorate(decoratingOptions, client)) {
      decoratingOptions = filterDecoratingOptions(decoratingOptions, client);
      decorate(decoratingOptions, client);
    }

    return client;
  }

  @Override
  public E loadUserByUsername(String username) {
    return delegate.loadUserByUsername(username);
  }

  @Override
  public E save(E entity) {
    return delegate.save(entity);
  }

  @Override
  public void delete(String username) {
    delegate.delete(username);
  }

  @Override
  public void changePassword(String newPassword, String username) {
    delegate.changePassword(newPassword, username);
  }

  private void decorate(Set<DecoratingOptions> decoratingOptions, E clientEntity) {
    for (DecoratingOptions option : decoratingOptions) {
      switch (option) {
        case SUBSCRIPTIONS -> decorateWithSubscriptions(clientEntity);
        case AUTHENTICATION_LOGS -> decorateWithAuthenticationLogs(clientEntity);
        case BLOCKING_AUTHENTICATION_LOGS -> decorateWithBlockingAuthenticationLogs(clientEntity);
        case COMPANY_REPRESENTATIVES, REPRESENTATIVE_COMPANY -> decorateWithClients(clientEntity);
      }
    }

  }

  abstract void decorateWithClients(E clientEntity);

  private void decorateWithAuthenticationLogs(E clientEntity) {
    List<AuthenticationLogEntity> clientAuthenticationLogs = authenticationLogService.getClientAuthenticationLogs(clientEntity.getId());
    clientEntity.setAuthenticationLogs(clientAuthenticationLogs);
  }

  private void decorateWithSubscriptions(E clientEntity) {
    List<SubscriptionEntity> clientSubscriptions = subscriptionService.getClientSubscriptions(clientEntity.getId());
    clientEntity.setSubscriptions(clientSubscriptions);
  }

  private void decorateWithBlockingAuthenticationLogs(E clientEntity) {
    List<AuthenticationLogEntity> clientAuthenticationLogs = authenticationLogService.getBlockingAuthenticationLogs(clientEntity.getUsername());
    clientEntity.setAuthenticationLogs(clientAuthenticationLogs);
  }

  private Set<DecoratingOptions> filterDecoratingOptions(Set<DecoratingOptions> decoratingOptions, E client) {
    return decoratingOptions.stream()
        .filter(option -> option.allowedForClient(client))
        .collect(Collectors.toSet());
  }

  private boolean shouldDecorate(Set<DecoratingOptions> options, E client) {
    return client != null && (ObjectsHelper.isNotEmpty(options));
  }

}

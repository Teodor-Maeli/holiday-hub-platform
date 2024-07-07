package com.platform.service;

import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.entity.Customer;
import com.platform.persistence.entity.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base decorator class that utilize the decorator pattern.
 *
 * @param <E> Entity
 */
@RequiredArgsConstructor
public abstract class CustomerDecorator<E extends Customer> implements CustomerService<E> {

  private final SubscriptionService subscriptionService;
  private final AuthenticationAttemptService authenticationAttemptService;
  private final CustomerService<E> delegate;

  abstract void decorateWithCustomers(E clientEntity);

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
        case AUTHENTICATION_ATTEMPTS -> decorateWithAuthAttempts(clientEntity);
        case BLOCKING_AUTHENTICATION_ATTEMPTS -> decorateWithBlockingAuthAttempts(clientEntity);
        case COMPANY_REPRESENTATIVES, REPRESENTATIVE_COMPANY -> decorateWithCustomers(clientEntity);
      }
    }

  }

  private void decorateWithAuthAttempts(E clientEntity) {
    List<AuthenticationAttempt> attempts = authenticationAttemptService.getAuthenticationAttempts(clientEntity.getId());
    clientEntity.setAuthenticationAttempts(attempts);
  }

  private void decorateWithSubscriptions(E clientEntity) {
    List<Subscription> subscriptions = subscriptionService.getSubscriptions(clientEntity.getId());
    clientEntity.setSubscriptions(subscriptions);
  }

  private void decorateWithBlockingAuthAttempts(E clientEntity) {
    List<AuthenticationAttempt> attempts = authenticationAttemptService.getBlockingAuthenticationAttempts(clientEntity.getUsername());
    clientEntity.setAuthenticationAttempts(attempts);
  }

  private Set<DecoratingOptions> filterDecoratingOptions(Set<DecoratingOptions> decoratingOptions, E client) {
    return decoratingOptions.stream()
        .filter(option -> option.getEligibleForDecorating().contains(client.getClass()))
        .collect(Collectors.toSet());
  }

  private boolean shouldDecorate(Set<DecoratingOptions> options, E client) {
    return client != null && !CollectionUtils.isEmpty(options);
  }

}

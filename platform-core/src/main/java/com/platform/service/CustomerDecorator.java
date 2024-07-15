package com.platform.service;

import com.platform.model.AuthenticationAttemptResource;
import com.platform.model.CustomerResource;
import com.platform.model.SubscriptionResource;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base decorator.
 */
@RequiredArgsConstructor
public abstract class CustomerDecorator implements CustomerService {

  private final SubscriptionService subscriptionService;
  private final AuthenticationAttemptService authenticationAttemptService;
  private final CustomerService delegate;

  @Override
  public CustomerResource loadUserByUsernameForDecoration(Set<DecoratingOptions> decoratingOptions, String username) {
    CustomerResource client = delegate.retrieve(username);

    if (shouldDecorate(decoratingOptions, client)) {
      decoratingOptions = filterDecoratingOptions(decoratingOptions, client);
      decorate(decoratingOptions, client);
    }

    return client;
  }

  @Override
  public CustomerResource retrieve(String username) {
    return delegate.retrieve(username);
  }

  @Override
  public CustomerResource create(CustomerResource customer) {
    return delegate.create(customer);
  }

  private void decorate(Set<DecoratingOptions> decoratingOptions, CustomerResource customer) {
    for (DecoratingOptions option : decoratingOptions) {
      switch (option) {
        case SUBSCRIPTIONS -> decorateWithSubscriptions(customer);
        case AUTHENTICATION_ATTEMPTS -> decorateWithAuthAttempts(customer);
        case BLOCKING_AUTHENTICATION_ATTEMPTS -> decorateWithBlockingAuthAttempts(customer);
      }
    }

  }

  private void decorateWithAuthAttempts(CustomerResource customer) {
    List<AuthenticationAttemptResource> attempts = authenticationAttemptService.getAuthenticationAttempts(customer.getId());
    customer.setAuthenticationAttempts(attempts);
  }

  private void decorateWithSubscriptions(CustomerResource customer) {
    List<SubscriptionResource> subscriptions = subscriptionService.getSubscriptions(customer.getId());
    customer.setSubscriptions(subscriptions);
  }

  private void decorateWithBlockingAuthAttempts(CustomerResource customer) {
    List<AuthenticationAttemptResource> attempts = authenticationAttemptService.getBlockingAuthenticationAttempts(customer.getUsername());
    customer.setAuthenticationAttempts(attempts);
  }

  private Set<DecoratingOptions> filterDecoratingOptions(Set<DecoratingOptions> decoratingOptions, CustomerResource customer) {
    return decoratingOptions.stream()
        .filter(option -> option.getEligibleForDecorating().contains(customer.getClass()))
        .collect(Collectors.toSet());
  }

  private boolean shouldDecorate(Set<DecoratingOptions> options, CustomerResource customer) {
    return customer != null && !CollectionUtils.isEmpty(options);
  }

}

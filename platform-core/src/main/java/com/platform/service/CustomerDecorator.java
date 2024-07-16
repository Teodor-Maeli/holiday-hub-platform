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
  public CustomerResource create(CustomerResource resource) {
    return delegate.create(resource);
  }

  @Override
  public CustomerResource update(CustomerResource resource) {
    return delegate.update(resource);
  }

  @Override
  public CustomerResource retrieve(String username) {
    return delegate.retrieve(username);
  }

  @Override
  public CustomerResource retrieve(Set<DecoratingOptions> decoratingOptions, String username) {
    CustomerResource resource = delegate.retrieve(username);

    if (shouldDecorate(decoratingOptions, resource)) {
      decoratingOptions = filterDecoratingOptions(decoratingOptions, resource);
      decorate(decoratingOptions, resource);
    }

    return resource;
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

  private void decorateWithAuthAttempts(CustomerResource resource) {
    List<AuthenticationAttemptResource> attempts = authenticationAttemptService.getAuthenticationAttempts(resource.getId());
    resource.setAuthenticationAttempts(attempts);
  }

  private void decorateWithSubscriptions(CustomerResource resource) {
    List<SubscriptionResource> subscriptions = subscriptionService.getSubscriptions(resource.getId());
    resource.setSubscriptions(subscriptions);
  }

  private void decorateWithBlockingAuthAttempts(CustomerResource resource) {
    List<AuthenticationAttemptResource> attempts = authenticationAttemptService.getBlockingAuthenticationAttempts(resource.getUsername());
    resource.setAuthenticationAttempts(attempts);
  }

  private Set<DecoratingOptions> filterDecoratingOptions(Set<DecoratingOptions> decoratingOptions, CustomerResource resource) {
    return decoratingOptions.stream()
        .filter(option -> option.getEligibleForDecorating().contains(resource.getClass()))
        .collect(Collectors.toSet());
  }

  private boolean shouldDecorate(Set<DecoratingOptions> options, CustomerResource customer) {
    return customer != null && !CollectionUtils.isEmpty(options);
  }

}

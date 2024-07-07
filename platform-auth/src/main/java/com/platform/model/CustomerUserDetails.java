package com.platform.model;

import com.platform.exception.PlatformBackendException;
import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.entity.Customer;
import com.platform.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record CustomerUserDetails(Customer client) implements UserDetails {

  public CustomerUserDetails {
    if (client == null) {
      throw new PlatformBackendException().setMessage("Authentication was aborted!").setDetails("Client object cannot be null!");
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return SecurityUtils.toSimpleGrantedAuthorities(client.getConsumerAuthorities());
  }

  @Override
  public String getPassword() {
    return client.getPassword();
  }

  @Override
  public String getUsername() {
    return client.getUsername();
  }

  @Override
  public boolean isAccountNonLocked() {
    return client.getAuthenticationAttempts().stream().noneMatch(this::isAutoLocked)
        && !Boolean.TRUE.equals(client.getAccountLocked());
  }

  public boolean isAdmin() {
    return client.getConsumerAuthorities().contains(ConsumerAuthority.ADMIN);
  }

  private boolean isAutoLocked(AuthenticationAttempt attempt) {
    return (attempt.getAuthenticationStatus() == AuthenticationStatus.LOCKED
            || attempt.getAuthenticationStatus() == AuthenticationStatus.BLACKLISTED
            || attempt.getStatusReason().equals(AuthenticationStatusReason.BAD_CREDENTIALS)) && Boolean.FALSE.equals(attempt.getStatusResolved());
  }

}

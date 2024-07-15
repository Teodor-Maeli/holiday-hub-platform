package com.platform.model;

import com.platform.exception.PlatformBackendException;
import com.platform.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record CustomerUserDetails(CustomerResource customer) implements UserDetails {

  public CustomerUserDetails {
    if (customer == null) {
      throw new PlatformBackendException()
          .setMessage("Authentication was aborted!")
          .setDetails("Client object cannot be null!");
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return SecurityUtils.toSimpleGrantedAuthorities(customer.getAuthorities());
  }

  @Override
  public String getPassword() {
    return customer.getPassword();
  }

  @Override
  public String getUsername() {
    return customer.getUsername();
  }

  @Override
  public boolean isAccountNonLocked() {
    return customer.getAuthenticationAttempts().stream().noneMatch(this::isAutoLocked)
        && !Boolean.TRUE.equals(customer.getLocked());
  }

  public boolean isAdmin() {
    return customer.getAuthorities().contains(ConsumerAuthority.ADMIN);
  }

  private boolean isAutoLocked(AuthenticationAttemptResource attempt) {
    return (attempt.getAuthenticationStatus() == AuthenticationStatus.LOCKED
        || attempt.getAuthenticationStatus() == AuthenticationStatus.BLACKLISTED
        || attempt.getStatusReason().equals(AuthenticationStatusReason.BAD_CREDENTIALS)) && Boolean.FALSE.equals(attempt.getStatusResolved());
  }

}

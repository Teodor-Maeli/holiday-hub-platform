package com.platform.model;

import com.platform.exception.PlatformBackendException;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.CustomerEntity;
import com.platform.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

public record ClientUserDetails(CustomerEntity client) implements UserDetails {

  public ClientUserDetails {
    if (client == null) {
      throw new PlatformBackendException().setMessage("Authentication was aborted!").setDetails("Client object cannot be null!");
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return SecurityUtils.toSimpleGrantedAuthority(client.getConsumerAuthorities());
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
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return client.getAuthenticationLogs().stream().noneMatch(this::isAutoLocked)
        && !Boolean.TRUE.equals(client.getAccountLocked());
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !CollectionUtils.isEmpty(client.getAuthenticationLogs());
  }

  public boolean isAdmin() {
    return client.getConsumerAuthorities().contains(ConsumerAuthority.ADMIN);
  }

  private boolean isAutoLocked(AuthenticationLogEntity log) {
    return (log.getAuthenticationStatus() == AuthenticationStatus.LOCKED
            || log.getAuthenticationStatus() == AuthenticationStatus.BLACKLISTED
            || log.getStatusReason().equals(AuthenticationStatusReason.BAD_CREDENTIALS)) && Boolean.FALSE.equals(log.getStatusResolved());
  }

}

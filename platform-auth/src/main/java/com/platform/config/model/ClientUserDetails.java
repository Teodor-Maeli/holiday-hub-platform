package com.platform.config.model;

import com.platform.config.util.SecurityUtils;
import com.platform.exception.PlatformBackendException;
import com.platform.model.AuthenticationStatus;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record ClientUserDetails(ClientEntity client) implements UserDetails {

  public ClientUserDetails {
    if (client == null) {
      throw PlatformBackendException.builder()
          .message("Authentication was aborted!")
          .details("Client object cannot be null!")
          .build();
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
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    if (client.getConsumerAuthorities() == null || client.getConsumerAuthorities().isEmpty()) {
      return false;
    }

    return client.getAuthenticationAuditLogs().stream().noneMatch(this::isLocked);
  }

  private boolean isLocked(AuthenticationLogEntity log) {
    return (log.getAuthenticationStatus() == AuthenticationStatus.LOCKED
        || log.getAuthenticationStatus() == AuthenticationStatus.BLACKLISTED)
        && Boolean.FALSE.equals(log.getStatusResolved());
  }
}

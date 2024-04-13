package com.platform.model;

import com.platform.exception.PlatformBackendException;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.util.ObjectsHelper;
import com.platform.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.platform.model.ConsumerAuthority.ADMIN;

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
    return client.getAuthenticationLogs().stream().noneMatch(this::isAutoLocked)
        && !Boolean.TRUE.equals(client.getAccountLocked());
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return ObjectsHelper.isNotEmpty(client.getConsumerAuthorities());
  }

  public boolean isAdmin() {
    return client.getConsumerAuthorities().contains(ADMIN);
  }

  private boolean isAutoLocked(AuthenticationLogEntity log) {
    return (
        log.getAuthenticationStatus() == AuthenticationStatus.LOCKED
            || log.getAuthenticationStatus() == AuthenticationStatus.BLACKLISTED
            || log.getStatusReason().equals(AuthenticationStatusReason.BAD_CREDENTIALS)
    ) && Boolean.FALSE.equals(log.getStatusResolved());
  }

}

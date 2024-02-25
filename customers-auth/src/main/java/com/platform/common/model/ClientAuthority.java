package com.platform.common.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User Authorities used to determine capabilities.
 * Since 1.0
 */
public enum ClientAuthority {
  ADMIN,
  BASE_CLIENT,
  COMPANY_REPRESENTATIVE;

  public static SimpleGrantedAuthority toSimpleGrantedAuthority(ClientAuthority authority) {
    return new SimpleGrantedAuthority(authority.name());
  }

  public static Set<SimpleGrantedAuthority> toSimpleGrantedAuthority(Collection<ClientAuthority> authorities) {
    if (authorities == null) {
      return Collections.emptySet();
    }

    return authorities.stream()
        .map(ClientAuthority::toSimpleGrantedAuthority)
        .collect(Collectors.toSet());
  }


}

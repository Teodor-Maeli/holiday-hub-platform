package com.platform.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public final class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public JWTAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
    super(principal, null, authorities);
  }

}

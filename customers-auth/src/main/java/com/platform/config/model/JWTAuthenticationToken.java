package com.platform.config.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

  private String subject;

  public JWTAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String subject) {
    super(authorities);
    this.subject = subject;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return subject;
  }
}

package com.platform.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.platform.model.ClientUserDetails;
import com.platform.model.ConsumerAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {

  public static Algorithm getSignAlgorithm() {
    return Algorithm.HMAC256("key");
  }

  public static SimpleGrantedAuthority toSimpleGrantedAuthority(ConsumerAuthority authority) {
    return new SimpleGrantedAuthority(authority.name());
  }

  public static Set<SimpleGrantedAuthority> toSimpleGrantedAuthority(Collection<ConsumerAuthority> authorities) {
    if (authorities == null) {
      return Collections.emptySet();
    }

    return authorities.stream()
        .map(SecurityUtils::toSimpleGrantedAuthority)
        .collect(Collectors.toSet());
  }

  public static Optional<ClientUserDetails> getPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() instanceof ClientUserDetails details) {
      return Optional.of(details);
    }

    return Optional.empty();

  }

}

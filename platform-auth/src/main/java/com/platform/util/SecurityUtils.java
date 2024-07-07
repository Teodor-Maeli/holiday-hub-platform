package com.platform.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.platform.model.ConsumerAuthority;
import com.platform.model.CustomerUserDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

  public static Algorithm getSignAlgorithm() {
    return Algorithm.HMAC256("key");
  }

  public static SimpleGrantedAuthority toSimpleGrantedAuthority(ConsumerAuthority authority) {
    return new SimpleGrantedAuthority(authority.name());
  }

  public static Set<SimpleGrantedAuthority> toSimpleGrantedAuthorities(Collection<ConsumerAuthority> authorities) {
    if (!CollectionUtils.isEmpty(authorities)) {
      return authorities.stream().map(SecurityUtils::toSimpleGrantedAuthority)
          .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }

  public static Optional<CustomerUserDetails> getPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() instanceof CustomerUserDetails details) {
      return Optional.of(details);
    }

    return Optional.empty();
  }

}

package com.platform.component;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.PlatformSecurityProperties;
import com.platform.model.CustomerUserDetails;
import com.platform.model.JWTComposite;
import com.platform.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTGenerator {

  private final ObjectMapper objectMapper;
  private final PlatformSecurityProperties properties;

  public JWTComposite generate(CustomerUserDetails client) {
    String accessToken = generateJWT(5000000L, client);
    String refreshToken = generateJWT(5000000L + 100000L, client);

    return JWTComposite.of(accessToken, refreshToken);
  }

  private String generateJWT(Long expirationTime, CustomerUserDetails details) {

    try {
      return JWT.create()
          .withIssuer(properties.getJwtIssuer())
          .withSubject(details.getUsername())
          .withClaim("email", details.client().getEmailAddress())
          .withClaim("roles", toJson(details.getAuthorities()))
          .withIssuedAt(new Date())
          .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
          .withJWTId(UUID.randomUUID().toString())
          .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
          .sign(SecurityUtils.getSignAlgorithm());
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException("Failure during JWT generation for username:" + details.getUsername(), e);
    }
  }

  private String toJson(Collection<? extends GrantedAuthority> authorities) throws JsonProcessingException {
    return objectMapper.writeValueAsString(authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet()));
  }

}

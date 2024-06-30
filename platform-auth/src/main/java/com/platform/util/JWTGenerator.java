package com.platform.util;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.PlatformSecurityProperties;
import com.platform.model.ClientUserDetails;
import com.platform.model.JWTComposite;
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

  private static final String EMAIL = "email";
  private static final Long ACCESS_TOKEN_EXPIRATION = 5000000L;
  private static final Long REFRESH_TOKEN_EXPIRATION = ACCESS_TOKEN_EXPIRATION + 100000L;

  private final ObjectMapper objectMapper;
  private final PlatformSecurityProperties properties;

  public JWTComposite generate(ClientUserDetails client) {
    String accessToken = generateAccessToken(client);
    String refreshToken = generateRefreshToken(client);

    return JWTComposite.of(accessToken, refreshToken);
  }

  private String generateRefreshToken(ClientUserDetails details) {
    return generateJWT(REFRESH_TOKEN_EXPIRATION, details);
  }

  private String generateAccessToken(ClientUserDetails details) {
    return generateJWT(ACCESS_TOKEN_EXPIRATION, details);
  }

  private String generateJWT(Long expirationTime, ClientUserDetails details) {

    try {
      return JWT.create()
          .withIssuer(properties.getJwtIssuer())
          .withSubject(details.getUsername())
          .withClaim(EMAIL, details.client().getEmailAddress())
          .withClaim("roles", toJson(details.getAuthorities()))
          .withIssuedAt(new Date())
          .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
          .withJWTId(UUID.randomUUID().toString())
          .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
          .sign(SecurityUtils.getSignAlgorithm());
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException("Failed to authenticate client username:" + details.getUsername(), e);
    }
  }

  private String toJson(Collection<? extends GrantedAuthority> authorities) throws JsonProcessingException {
    return objectMapper.writeValueAsString(authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet()));
  }

}

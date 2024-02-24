package com.platform.config.util;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.config.model.JWTComposite;
import com.platform.domain.entity.Client;
import com.platform.common.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JWTGenerator {

  private static final String EMAIL = "email";

  private static final Long ACCESS_TOKEN_EXPIRATION = 5000000L;

  private static final Long REFRESH_TOKEN_EXPIRATION = ACCESS_TOKEN_EXPIRATION + 100000L;

  private final ObjectMapper objectMapper;

  private final JWTUtil jwtUtil;

  @Value("${platform.security.jwt-issuer}")
  private String issuer;

  public JWTGenerator(ObjectMapper objectMapper, JWTUtil jwtUtil) {
    this.objectMapper = objectMapper;
    this.jwtUtil = jwtUtil;
  }

  public JWTComposite generate(Client client) {
    String accessToken = generateAccessToken(client);
    String refreshToken = generateRefreshToken(client);

    return JWTComposite.of(accessToken, refreshToken);
  }

  private String generateRefreshToken(Client client) {
    return generateJWT(REFRESH_TOKEN_EXPIRATION, client);
  }

  private String generateAccessToken(Client client) {
    return generateJWT(ACCESS_TOKEN_EXPIRATION, client);
  }

  private String generateJWT(Long expirationTime, Client client) {

    try {
      return JWT.create()
          .withIssuer(issuer)
          .withSubject(client.getId().toString())
          .withClaim(EMAIL, client.getUsername())
          .withClaim("roles", toJson(client.getRoles()))
          .withIssuedAt(new Date())
          .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
          .withJWTId(UUID.randomUUID().toString())
          .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
          .sign(jwtUtil.getSignAlgorithm());
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException("Failed to authenticate client with id:" + client.getId(), e);
    }
  }

  private String toJson(Set<Role> roles) throws JsonProcessingException {
    return objectMapper.writeValueAsString(roles);
  }

}

package com.platform.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTComposite {

  private final String accessToken;
  private final String refreshToken;

  public static JWTComposite of(String accessToken, String refreshToken) {
    return new JWTComposite(accessToken, refreshToken);
  }

}

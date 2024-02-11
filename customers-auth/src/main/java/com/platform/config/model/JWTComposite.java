package com.platform.config.model;

public class JWTComposite {

  private final String accessToken;

  private final String refreshToken;

  private JWTComposite(
      String accessToken,
      String refreshToken
  ) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public static JWTComposite of(String accessToken, String refreshToken) {
    return new JWTComposite(accessToken, refreshToken);
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

}

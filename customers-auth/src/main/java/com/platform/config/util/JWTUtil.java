package com.platform.config.util;

import com.auth0.jwt.algorithms.Algorithm;

public class JWTUtil {

  public Algorithm getSignAlgorithm() {
    return Algorithm.HMAC256("key");
  }

}

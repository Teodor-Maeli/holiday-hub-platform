package com.platform.config.util;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

  public Algorithm getSignAlgorithm() {
    return Algorithm.HMAC256("key");
  }

}

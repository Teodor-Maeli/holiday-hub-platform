package com.platform.util;

import com.platform.service.Encoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthEncoder implements Encoder {

  private final PasswordEncoder encoder;

  public AuthEncoder(PasswordEncoder encoder) {this.encoder = encoder;}

  @Override
  public String encode(String raw) {
    return encoder.encode(raw);
  }

}

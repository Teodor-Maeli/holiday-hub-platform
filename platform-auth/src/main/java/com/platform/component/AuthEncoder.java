package com.platform.component;

import com.platform.service.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEncoder implements Encoder {

  private final PasswordEncoder encoder;

  @Override
  public String encode(String raw) {
    return encoder.encode(raw);
  }

  @Override
  public boolean matches(String raw, String hashed) {
    return encoder.matches(raw, hashed);
  }

}

package com.platform.mapper;

import com.platform.service.Encoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderMapper {

  private final Encoder encoder;

  public PasswordEncoderMapper(Encoder encoder) {
    this.encoder = encoder;
  }

  @EncodedMapping
  public String encode(String value) {
    return encoder.encode(value);
  }

}

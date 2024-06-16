package com.platform.mapper;

import com.platform.service.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

  private final Encoder encoder;

  @EncodedMapping
  public String encode(String value) {
    return encoder.encode(value);
  }

}

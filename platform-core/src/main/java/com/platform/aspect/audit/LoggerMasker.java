package com.platform.aspect.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerMasker {

  private static final String MASK = "*******";
  private static final ObjectMapper MAPPER = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT)
      .findAndRegisterModules();
  private static final List<Pattern> MASKING_PATTERNS;

  static {
    MASKING_PATTERNS = new ArrayList<>();
    MASKING_PATTERNS.add(Pattern.compile("(?<=password\" : \")(.*?)(?=\")"));
    MASKING_PATTERNS.add(Pattern.compile("(?<=emailAddress\" : \")(.*?)(?=\")"));
    MASKING_PATTERNS.add(Pattern.compile("(?<=password\" : \")(.*?)(?=\")"));
    MASKING_PATTERNS.add(Pattern.compile("(?<=familyName\" : \")(.*?)(?=\")"));
    MASKING_PATTERNS.add(Pattern.compile("(?<=givenName\" : \")(.*?)(?=\")"));
    MASKING_PATTERNS.add(Pattern.compile("(?<=middleName\" : \")(.*?)(?=\")"));
    MASKING_PATTERNS.add(Pattern.compile("(?<=unlockingCode\" : \")(.*?)(?=\")"));
  }

  public static String mask(Object o) {
    return toJson(o)
        .map(LoggerMasker::maskInternal)
        .orElse(null);
  }

  private static String maskInternal(String value) {

    for (Pattern pattern : MASKING_PATTERNS) {
      value = pattern.matcher(value).replaceAll(MASK);
    }

    return value;
  }

  private static Optional<String> toJson(Object o) {
    try {
      return Optional.ofNullable(MAPPER.writeValueAsString(o));
    } catch (JsonProcessingException e) {
      log.error("JsonMaskUtil failed to write as string!", e);
      return Optional.empty();
    }
  }
}

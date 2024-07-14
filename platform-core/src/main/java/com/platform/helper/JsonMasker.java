package com.platform.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.model.JsonMaskerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JsonMasker {

  private final ObjectMapper mapper;
  private final List<Pattern> patterns;
  private final String mask;

  public JsonMasker(ObjectMapper mapper, JsonMaskerProperties properties) {
    this.mapper = mapper;

    this.mask = properties.getPattern().repeat(properties.getFrequency());
    this.patterns = properties.getFields().stream()
        .map(field -> properties.getRegex().replace("{field}", field))
        .map(Pattern::compile)
        .collect(Collectors.toList());
  }

  public String mask(Object o) {
    return toJson(o).map(this::maskInternal).orElse(null);
  }

  private String maskInternal(String value) {
    for (Pattern pattern : patterns) {
      value = pattern.matcher(value).replaceAll(mask);
    }

    return value;
  }

  private Optional<String> toJson(Object o) {
    try {
      return Optional.ofNullable(mapper.writeValueAsString(o));
    } catch (JsonProcessingException e) {
      log.error("JsonMaskUtil failed to write as string!", e);
      return Optional.empty();
    }
  }
}

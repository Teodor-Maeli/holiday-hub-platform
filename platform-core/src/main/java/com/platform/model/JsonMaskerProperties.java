package com.platform.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "masker")
public class JsonMaskerProperties {

  private String pattern;
  private String regex;
  private int frequency;
  private List<String> fields;
}

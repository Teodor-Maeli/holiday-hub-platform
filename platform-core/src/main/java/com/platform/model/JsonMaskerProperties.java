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

  String pattern;
  String regex;
  int frequency;
  List<String> fields;
}

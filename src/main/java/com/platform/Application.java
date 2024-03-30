package com.platform;

import com.platform.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@PropertySource(
    value = {
        "classpath:application-auth.yml",
        "classpath:application-core.yml",
        "classpath:application-customers-resources.yml"
    },
    factory = YamlPropertySourceFactory.class)
@EnableJpaAuditing
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}

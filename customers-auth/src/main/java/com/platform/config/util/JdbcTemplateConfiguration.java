package com.platform.config.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.Executors;

/**
 * Creates bean of {@link ImprovedJdbcTemplate} with required configurations.
 */
@Configuration
public class JdbcTemplateConfiguration {

  @Value("${spring.datasource.driver-class-name}")
  private String driver;

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;


  @Bean(value = "improvedJdbcTemplate")
  public JdbcTemplate jdbcTemplate() {
    final PlatformSecondaryDataSource ds = new PlatformSecondaryDataSource();
    ds.setMaximumPoolSize(100);
    ds.setDriverClassName(driver);
    ds.setJdbcUrl(url);
    ds.setUsername(username);
    ds.setPassword(password);

    return new ImprovedJdbcTemplate(ds);
  }

}

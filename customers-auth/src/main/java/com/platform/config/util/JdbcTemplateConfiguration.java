package com.platform.config.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Purpose of this is to disable auto commit of jdbc template.
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
    final PlatformDataSource ds = new PlatformDataSource();
    ds.setMaximumPoolSize(100);
    ds.setDriverClassName(driver);
    ds.setJdbcUrl(url);
    ds.setUsername(username);
    ds.setPassword(password);

    return new ImprovedJdbcTemplate(ds);
  }

  public static class ImprovedJdbcTemplate extends JdbcTemplate {

    public ImprovedJdbcTemplate(PlatformDataSource dataSource) {
      super(dataSource);
    }

    public Connection getConnection() throws SQLException {
      DataSource dataSource = getDataSource();

      if (dataSource != null) {
        return DataSourceUtils.getConnection(getDataSource());
      } else {
        throw new SQLException("No set datasource, could not obtain connection!");
      }
    }
  }


  private static class PlatformDataSource extends HikariDataSource {

    public PlatformDataSource() {
    }

    public PlatformDataSource(HikariConfig configuration) {
      super(configuration);
    }

    @Override
    public Connection getConnection() throws SQLException {
      Connection connection = super.getConnection();
      connection.setAutoCommit(false);
      return connection;
    }
  }
}

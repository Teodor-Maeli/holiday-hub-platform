package com.platform.config.util;

import com.platform.exception.UncheckedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Extends logic of {@link org.springframework.jdbc.core.JdbcTemplate},
 * disables auto commit and adding additional logic, relays on {@link PlatformSecondaryDataSource}
 */
public class ImprovedJdbcTemplate extends JdbcTemplate {

  public ImprovedJdbcTemplate(PlatformSecondaryDataSource dataSource) {
    super(dataSource);
  }

  public void commit() {
    try {
      Connection connection = getConnection();
      connection.commit();
    } catch (SQLException e) {
      throw new UncheckedSQLException(e);
    }
  }

  public void close() {
    try {
      Connection connection = getConnection();
      connection.close();
    } catch (SQLException e) {
      throw new UncheckedSQLException(e);
    }
  }

  public void rollback() {
    try {
      Connection connection = getConnection();
      connection.rollback();
    } catch (SQLException e) {
      throw new UncheckedSQLException(e);
    }
  }

  private Connection getConnection() throws SQLException {
    DataSource dataSource = getDataSource();

    if (dataSource != null) {
      return dataSource.getConnection();
    } else {
      throw new SQLException("Datasource is null, could not obtain connection!");
    }
  }
}

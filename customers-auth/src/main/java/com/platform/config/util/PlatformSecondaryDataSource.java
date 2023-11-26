package com.platform.config.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

class PlatformSecondaryDataSource extends HikariDataSource implements SmartDataSource {

  private volatile static Cache<String, Connection> cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(60, TimeUnit.SECONDS)
      .removalListener(new ConnectionsRemovalListener())
      .build();

  public PlatformSecondaryDataSource() {
  }

  public PlatformSecondaryDataSource(HikariConfig configuration) {
    super(configuration);
  }

  @Override
  public Connection getConnection() throws SQLException {
    Connection connection = cache.getIfPresent(getKey());

    if (connection != null && ! connection.isClosed()) {
      return connection;
    }

    connection = super.getConnection();
    connection.setAutoCommit(false);
    cache.put(getKey(), connection);
    return connection;
  }

  @Override
  public boolean shouldClose(Connection con) {
    // We don't care if connections should be closed, we want re-use JdbcTemplate logic, without closing the connection.
    // Connections will be closed explicitly on completion of the statements.
    return false;
  }

  private String getKey() {
    Thread thread = Thread.currentThread();
    return thread.getName() + thread.getId();
  }

}

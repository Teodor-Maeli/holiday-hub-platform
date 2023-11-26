package com.platform.config.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.SmartDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

class PlatformSecondaryDataSource extends HikariDataSource implements SmartDataSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlatformSecondaryDataSource.class);

  private volatile static Cache<String, Connection> cache = CacheBuilder.newBuilder()
      .maximumSize(Integer.MAX_VALUE)
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

  private String getKey() {
    Thread thread = Thread.currentThread();
    return thread.getName() + thread.getId();
  }

  @Override
  public boolean shouldClose(Connection con) {
    // We don't care if connections should be closed, we want re-use JdbcTemplate logic, without closing the connection.
    // Connections will be closed explicitly on completion of the statements.
    return false;
  }

  private static class ConnectionsRemovalListener implements RemovalListener<String, Connection> {

    public ConnectionsRemovalListener() {
    }

    @Override
    public void onRemoval(RemovalNotification<String, Connection> notification) {
      if (notification.getValue() != null) {
        try {
          Connection conn = notification.getValue();

          if (! conn.isClosed()) {
            conn.close();
            LOGGER.info("Expired a  jdbc connection created from thread " + notification.getValue());
          }
        } catch (SQLException e) {
          LOGGER.error("Failed to close connection on cache expiration!");
        }
      }
    }
  }
}

package com.platform.config.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.SmartDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Util DataSource, to be used only with {@link ImprovedJdbcTemplate}
 */
public class PlatformSecondaryDataSource extends HikariDataSource implements SmartDataSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlatformSecondaryDataSource.class);
  private static PlatformSecondaryDataSource instance;
  private volatile Cache<String, Connection> cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(60, TimeUnit.SECONDS)
      .removalListener(new ConnectionsRemovalListener())
      .build();

  private PlatformSecondaryDataSource() {
    scheduleExpirationCleanUp();
  }

  public static synchronized PlatformSecondaryDataSource getInstance() {
    if (instance == null) {
      instance = new PlatformSecondaryDataSource();
    }

    return instance;
  }

  /**
   * Gets cached or fresh database connection and caches it.
   *
   * @return {@link Connection}
   * @throws SQLException If fails to fetch fresh connection from the pool.
   */
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

  /**
   * Clears cache.
   */
  public void clearCache() {
    cache.invalidateAll();
    cache.cleanUp();
  }

  private String getKey() {
    Thread thread = Thread.currentThread();
    return thread.getName() + thread.getId();
  }

  private void scheduleExpirationCleanUp() {
    //We need cleanup cache from expired connections, so we schedule a task.
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    Runnable cleaner = () -> {
      try {
        cache.cleanUp();
      } catch (Exception e) {
        LOGGER.error("A clean up error has occurred on the connections cache, please pay attention!", e);
      }
    };

    scheduler.scheduleAtFixedRate(cleaner, 10, 10, TimeUnit.SECONDS);
  }

}

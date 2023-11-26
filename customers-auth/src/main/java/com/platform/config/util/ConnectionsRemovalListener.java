package com.platform.config.util;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionsRemovalListener implements RemovalListener<String, Connection> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlatformSecondaryDataSource.class);

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

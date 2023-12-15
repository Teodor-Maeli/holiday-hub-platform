package com.platform.rest.controller;

import com.platform.aspect.logger.IOLogger;
import com.platform.config.util.PlatformSecondaryDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = AdminController.CUSTOMERS_AUTH_V_1_ADMIN,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AdminController {

  public static final String CUSTOMERS_AUTH_V_1_ADMIN = "/customers-auth/v1/admin";

  public static final String DATASOURCE_SECONDARY_CLEAR_CACHE = "/datasource/secondary/clear-cache";

  @IOLogger
  @PostMapping(path = DATASOURCE_SECONDARY_CLEAR_CACHE)
  public ResponseEntity<Void> clearSecondaryDatasourceCache() {
    PlatformSecondaryDataSource ds = PlatformSecondaryDataSource.getInstance();
    ds.clearCache();

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}

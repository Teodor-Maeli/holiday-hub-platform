package com.platform.service;

import com.platform.common.model.AggregationOptions;
import com.platform.domain.entity.AuthenticationAuditLog;
import com.platform.domain.entity.Client;
import com.platform.domain.entity.Subscription;
import com.platform.domain.repository.BaseClientRepository;
import com.platform.exception.PlatformBackendException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Abstract service class that will provide basic operations. To be extended by subclasses.
 *
 * @param <E>                               Entity
 * @param <ID>                              ID of entity
 * @param <R>                               Repository
 */
public abstract class AbstractClientService
    <E extends Client, ID, R extends BaseClientRepository<E, ID>> implements UserDetailsService {

  private final R repository;
  private final PasswordEncoder encoder;
  private final SubscriptionService subscriptionService;
  private final AuthenticationAuditLogService authenticationAuditLogService;

  /**
   * Ensures dependencies are properly initialized.
   *
   * @param repository                       Repository used to perform generic operations.
   * @param encoder                          Used to encode sensitive information.
   * @param subscriptionService              Subscriptions service.
   * @param authenticationAuditLogService    Authentication logs service.
   */
  protected AbstractClientService(
      R repository,
      PasswordEncoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationAuditLogService authenticationAuditLogService
  ) {
    this.repository = repository;
    this.encoder = encoder;
    this.subscriptionService = subscriptionService;
    this.authenticationAuditLogService = authenticationAuditLogService;
  }

  /**
   * Use to authenticate a user against the server.
   *
   * @param username                        The username identifying the user whose data is required.
   * @return {@link UserDetails}            The user details required in order to perform successful authentication.
   * @throws PlatformBackendException       If failed to load user with HTTP status 500 - Internal Server Error.
   */
  @Override
  public E loadUserByUsername(String username) {
    Optional<E> user = repository.findByUsernameAndNotLockedOrBlacklisted(username);

    if (user.isPresent()) {
      return user.get();
    }

    throw new PlatformBackendException("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username), BAD_REQUEST);
  }

  /**
   * Performs aggregations against client object.
   *
   * @param username                        The username identifying the user whose data is required.
   * @param aggregations             Aggregations objects to be included, like subscriptions and other.
   * @return {@link UserDetails}            The user details required in order to perform successful authentication.
   * @throws PlatformBackendException       If failed to load user with HTTP status 500 - Internal Server Error.
   */

  public E loadUserByUsernameAggregated(Set<AggregationOptions> aggregations, String username) {
    Optional<E> user = repository.findByUsername(username);

    if (user.isPresent()) {
      return doAggregate(aggregations, user.get());
    }

    throw new PlatformBackendException("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username), BAD_REQUEST);
  }

  private E doAggregate(Set<AggregationOptions> includeAggregations, E clientEntity) {
    if (! includeAggregations.isEmpty()) {

      for (AggregationOptions aggregation : includeAggregations) {
        switch (aggregation) {
          case SUBSCRIPTIONS -> clientEntity.setSubscriptions(getSubscriptions(clientEntity.getUsername()));
          case AUTHENTICATION_AUDIT_LOG -> clientEntity.setAuthenticationAuditLogs(getAuthenticationLogs(clientEntity.getUsername()));
        }
      }

    }

    return clientEntity;
  }

  private List<AuthenticationAuditLog> getAuthenticationLogs(String username) {
   return authenticationAuditLogService.getClientAuthenticationLogs(username);
  }

  private List<Subscription> getSubscriptions(String username) {
    return subscriptionService.getClientSubscriptions(username);
  }

  /**
   * Use to update an already existing account or persist a new.
   *
   * @param entity                          Customer information that is to be persisted/updated into the database.
   * @return {@link Client}                 The already persisted/updated customer from the database.
   * @throws PlatformBackendException       If failed to persist/update into the database with HTTP status 500 - Internal Server Error.
   */
  public E save(E entity) {
    try {
      return repository.save(entity);
    } catch (RuntimeException e) {
      throw new PlatformBackendException("Failed to SAVE entity with username: " + entity.getUsername(), INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Deletes permanently customer account from the database.
   *
   * @param username                        Identifier by which customer account has been deleted from database.
   */
  public void delete(String username) {
    if ((repository.deleteByUserName(username) <= 0)) {
      throw new PlatformBackendException("Failed to DELETE, USERNAME: %s non-existent!".formatted(username), BAD_REQUEST);
    }
  }

  /**
   * Updates customer account with encoded new password.
   *
   * @param newPassword                     Replacement for the current password.
   * @param username                        Username to query the database.
   * @throws PlatformBackendException       If fail to change password or 500 INTERNAL SERVER ERROR if another error occurs.
   */
  public void changePassword(String newPassword, String username) {
    try {
      String encoded = encoder.encode(newPassword);
      if (repository.updatePasswordByUsername(username, encoded) > 0) {
        return;
      }
      throw new PlatformBackendException("Failed to UPDATE password, USERNAME: %s non-existent!".formatted(username), BAD_REQUEST);
    } catch (RuntimeException e) {
      throw new PlatformBackendException("Failed to UPDATE password for USERNAME: %s".formatted(username), INTERNAL_SERVER_ERROR, e);
    }
  }

}

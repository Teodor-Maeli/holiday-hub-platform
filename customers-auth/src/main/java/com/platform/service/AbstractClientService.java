package com.platform.service;

import com.platform.common.model.DecoratingOptions;
import com.platform.common.model.ClientAuthority;
import com.platform.persistence.entity.Client;
import com.platform.persistence.repository.BaseClientRepository;
import com.platform.exception.PlatformBackendException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

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

   final R repository;
   final PasswordEncoder encoder;

  /**
   * Ensures dependencies are properly initialized.
   *
   * @param repository                       Repository used to perform generic operations.
   * @param encoder                          Used to encode sensitive information.
   */
  protected AbstractClientService(
      R repository,
      PasswordEncoder encoder
  ) {
    this.repository = repository;
    this.encoder = encoder;
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
   * @param decoratingOptions               Decorating options, like subscriptions and other.
   * @return {@link UserDetails}            The user details required in order to perform successful authentication.
   * @throws PlatformBackendException       If failed to load user with HTTP status 500 - Internal Server Error.
   */

  public E loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    Optional<E> user = repository.findByUsername(username);

    if (user.isPresent()) {
      return user.get();
    }

    throw new PlatformBackendException("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username), BAD_REQUEST);
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

      if(!repository.existsByUsername(entity.getUsername())) {
        entity.setAuthorities(Set.of(ClientAuthority.BASE_CLIENT));
      }

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

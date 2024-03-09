package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.ConsumerAuthority;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.repository.BaseClientRepository;
import com.platform.service.decorator.DecoratingOptions;
import org.hibernate.exception.ConstraintViolationException;

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
    <E extends ClientEntity, ID extends Number, R extends BaseClientRepository<E, ID>> {

   final R repository;
   final Encoder encoder;

  /**
   * Ensures dependencies are properly initialized.
   *
   * @param repository                       Repository used to perform generic operations.
   * @param encoder                          Used to encode sensitive information.
   */
  protected AbstractClientService(
      R repository,
      Encoder encoder
  ) {
    this.repository = repository;
    this.encoder = encoder;
  }

  /**
   * Use to authenticate a user against the server.
   *
   * @param username                        The username identifying the user whose data is required.
   * @return {@link Optional of client}     The user details required in order to perform successful authentication.
   */
  public Optional<E> loadClientByUsername(String username) {
    return repository.findByUsernameEligibleForLogin(username);
  }

  /**
   * Performs aggregations against client object.
   *
   * @param username                        The username identifying the user whose data is required.
   * @param decoratingOptions               Decorating options, like subscriptions and other.
   * @return {@link ClientEntity}                 The user details required in order to perform successful authentication.
   * @throws PlatformBackendException       If failed to load user with HTTP status 500 - Internal Server Error.
   */

  public E loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    Optional<E> user = repository.findByUsername(username);

    if (user.isPresent()) {
      return user.get();
    }

    throw PlatformBackendException.builder()
        .message("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
        .httpStatus(BAD_REQUEST)
        .build();

  }

  /**
   * Use to update an already existing account or persist a new.
   *
   * @param entity                          Customer information that is to be persisted/updated into the database.
   * @return {@link ClientEntity}                 The already persisted/updated customer from the database.
   * @throws PlatformBackendException       If failed to persist/update into the database with HTTP status 500 - Internal Server Error.
   */
  public E save(E entity) {
    try {

      if (! repository.existsByUsername(entity.getUsername())) {
        entity.setConsumerAuthorities(Set.of(ConsumerAuthority.BASE_CLIENT));
      }

      return repository.save(entity);
    } catch (ConstraintViolationException e) {

      throw PlatformBackendException.builder()
          .message("Failed to SAVE entity with username: %s, user existent".formatted(entity.getUsername()))
          .details(e.getMessage())
          .httpStatus(BAD_REQUEST)
          .cause(e)
          .build();

    } catch (RuntimeException e) {

      throw PlatformBackendException.builder()
          .message("Failed to SAVE entity with username: %s".formatted(entity.getUsername()))
          .details(e.getMessage())
          .httpStatus(INTERNAL_SERVER_ERROR)
          .cause(e)
          .build();
    }

  }

  /**
   * Deletes permanently customer account from the database.
   *
   * @param username                        Identifier by which customer account has been deleted from database.
   */
  public void delete(String username) {
    if ((repository.deleteByUserName(username) <= 0)) {
      throw PlatformBackendException.builder()
          .message("Failed to DELETE, USERNAME: %s non-existent!".formatted(username))
          .httpStatus(BAD_REQUEST)
          .build();
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

      throw PlatformBackendException.builder()
          .message("Failed to UPDATE password, USERNAME: %s non-existent!".formatted(username))
          .httpStatus(BAD_REQUEST)
          .build();

    } catch (RuntimeException e) {

      throw PlatformBackendException.builder()
          .message("Failed to UPDATE password for USERNAME: %s".formatted(username))
          .details(e.getMessage())
          .httpStatus(INTERNAL_SERVER_ERROR)
          .cause(e)
          .build();
    }

  }

}

package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.ConsumerAuthority;
import com.platform.persistence.entity.Customer;
import com.platform.persistence.repository.BaseClientRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Abstract service class that will provide basic operations. To be extended by subclasses.
 *
 * @param <E>  Entity
 * @param <ID> ID of entity
 * @param <R>  Repository
 */
@RequiredArgsConstructor
abstract class AbstractCustomerService
    <E extends Customer, ID extends Number, R extends BaseClientRepository<E, ID>> implements CustomerService<E> {

  final R repository;

  final Encoder encoder;

  /**
   * Performs aggregations against client object, to be used for general purposes except for login.
   *
   * @param username The username identifying the user whose data is required.
   * @return {@link Customer}                 The user details required in order to perform successful authentication.
   * @throws PlatformBackendException If failed to load user with HTTP status 500 - Internal Server Error.
   */
  @Override
  public E loadUserByUsername(String username) {
    Optional<E> user = repository.findByUsername(username);

    if (user.isPresent()) {
      return user.get();
    }

    throw new PlatformBackendException()
        .setMessage("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
        .setHttpStatus(BAD_REQUEST);

  }

  /**
   * Use to update an already existing account or persist a new.
   *
   * @param entity Customer information that is to be persisted/updated into the database.
   * @return {@link Customer}                 The already persisted/updated customer from the database.
   * @throws PlatformBackendException If failed to persist/update into the database with HTTP status 500 - Internal Server Error.
   */
  @Override
  public E save(E entity) {
    try {

      boolean isExisting = repository.existsByUsername(entity.getUsername());

      if (!isExisting) {
        entity.setConsumerAuthorities(Set.of(ConsumerAuthority.BASE_CLIENT));
      }

      return repository.save(entity);
    } catch (ConstraintViolationException e) {

      throw new PlatformBackendException()
          .setMessage("Failed to SAVE entity with username: %s, user existent".formatted(entity.getUsername()))
          .setDetails(e.getMessage())
          .setHttpStatus(BAD_REQUEST)
          .setCause(e);

    } catch (RuntimeException e) {

      throw new PlatformBackendException()
          .setMessage("Failed to SAVE entity with username: %s".formatted(entity.getUsername()))
          .setDetails(e.getMessage())
          .setHttpStatus(INTERNAL_SERVER_ERROR)
          .setCause(e);
    }

  }

  /**
   * Deletes permanently customer account from the database.
   *
   * @param username Identifier by which customer account has been deleted from database.
   */
  @Override
  public void delete(String username) {
    int updatedRows = repository.deleteByUserName(username);

    if (updatedRows <= 0) {
      throw new PlatformBackendException()
          .setMessage("Failed to DELETE, USERNAME: %s non-existent!".formatted(username))
          .setHttpStatus(BAD_REQUEST);
    }
  }

  /**
   * Updates customer account with encoded new password.
   *
   * @param newPassword Replacement for the current password.
   * @param username    Username to query the database.
   * @throws PlatformBackendException If fail to change password or 500 INTERNAL SERVER ERROR if another error occurs.
   */
  @Override
  public void changePassword(String newPassword, String username) {

    try {
      String encoded = encoder.encode(newPassword);
      int updatedRows = repository.updatePasswordByUsername(username, encoded);

      if (updatedRows > 0) {
        return;
      }

      throw new PlatformBackendException()
          .setMessage("Failed to UPDATE password, USERNAME: %s non-existent!".formatted(username))
          .setHttpStatus(BAD_REQUEST);

    } catch (RuntimeException e) {

      throw new PlatformBackendException()
          .setMessage("Failed to UPDATE password for USERNAME: %s".formatted(username))
          .setDetails(e.getMessage())
          .setHttpStatus(INTERNAL_SERVER_ERROR)
          .setCause(e);
    }

  }

}

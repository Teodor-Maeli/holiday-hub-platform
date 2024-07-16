package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.persistence.entity.Customer;
import com.platform.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Customer service class that will provide basic operations. To be extended by subclasses.
 */
@RequiredArgsConstructor
public class CustomerHelperService<T extends Customer, ID extends Number> {

  private final CustomerRepository<T, ID> repository;

  /**
   * Performs aggregations against client object, to be used for general purposes except for login.
   *
   * @param username The username identifying the user whose data is required.
   * @return {@link Customer}                 The user details required in order to perform successful authentication.
   * @throws PlatformBackendException If failed to load user with HTTP status 500 - Internal Server Error.
   */
  public T retrieve(String username) {
    Optional<T> customer = repository.findByUsername(username);

    if (customer.isPresent()) {
      return customer.get();
    }

    throw new PlatformBackendException()
        .setMessage("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
        .setHttpStatus(BAD_REQUEST);

  }

  /**
   * Creates customer.
   */
  public T create(T customer) {
    try {
      return repository.save(customer);
    } catch (ConstraintViolationException e) {

      throw new PlatformBackendException()
          .setMessage("Failed to SAVE entity with username: %s, user existent".formatted(customer.getUsername()))
          .setDetails(e.getMessage())
          .setHttpStatus(BAD_REQUEST)
          .setCause(e);

    } catch (RuntimeException e) {

      throw new PlatformBackendException()
          .setMessage("Failed to SAVE entity with username: %s".formatted(customer.getUsername()))
          .setDetails(e.getMessage())
          .setHttpStatus(INTERNAL_SERVER_ERROR)
          .setCause(e);
    }

  }

}

package com.platform.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.entity.PlatformClient;
import com.platform.domain.repository.PlatformBaseRepository;
import com.platform.exception.BackendException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Base service class that will provide basic crud operations. To be extended by subclasses.
 *
 * @param <E>  Entity
 * @param <ID> ID of entity
 * @param <R>  Repository
 */
public abstract class AbstractCRUDService
    <E extends PlatformClient, ID extends Number, R extends PlatformBaseRepository<E, ID>> {

    private final R repository;
    private final PasswordEncoder encoder;


    /**
     * Ensures all dependencies in the parent class are properly initialized.
     *
     * @param repository Repository used to perform generic operations.
     * @param encoder    Used to encode sensitive information.
     */
    public AbstractCRUDService(R repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    /**
     * User to update an already existing account or persist a new.
     *
     * @param entity Customer information that is to be persisited/updated into the database.
     * @return The already persisted/updated customer from the database.
     * @throws BackendException if failed to persist/update into the database with HTTP status 500 - Internal Server Error.
     */
    public E save(E entity) {
        try {
            return repository.save(entity);
        } catch (RuntimeException e) {
            throw new BackendException("Failed to SAVE entity with username: " + entity.getUsername(), INTERNAL_SERVER_ERROR, e);
        }
    }


    /**
     * Use to delete permanently customer from the database.
     *
     * @param id Identifier by which customer account has been deleted from database.
     */
    public void delete(ID id) {
        //probably need to change to deleteByUsername..
        try {
            repository.deleteById(id);
        } catch (RuntimeException e) {
            throw new BackendException("Failed to DELETE entity with ID: " + id, INTERNAL_SERVER_ERROR, e);
        }
    }


    /**
     * Updates customer password with new one and saves encoded.
     *
     * @param newPassword Replacement for the current password, not encoded,
     * @param username    Username to query the database.
     * @return {@link PlatformClient} entity after successful password change.
     * @throws BackendException with 400 BAD REQUEST if fail to change password.
     */
    public E changePassword(String newPassword, String username) {
        return repository
            .findByUserName(username)
            .map(entity -> {
                entity.setPassword(encoder.encode(newPassword));
                return repository.save(entity);
            })
            .orElseThrow(
                () -> new BackendException("Failed to UPDATE password for entity with username: " + username, BAD_REQUEST));
    }
}

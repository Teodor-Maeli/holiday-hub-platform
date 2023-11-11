package com.platform.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.entity.Client;
import com.platform.domain.repository.BaseClientRepository;
import com.platform.exception.BackendException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Abstract service class that will provide basic operations. To be extended by subclasses.
 *
 * @param <E>  Entity
 * @param <ID> ID of entity
 * @param <R>  Repository
 */
public abstract class AbstractClientService
    <E extends Client, ID, R extends BaseClientRepository<E, ID>> implements UserDetailsService {

    private final R repository;
    private final PasswordEncoder encoder;
    private final SessionRegistry sessionRegistry;

    /**
     * Ensures dependencies are properly initialized.
     *
     * @param repository      Repository used to perform generic operations.
     * @param encoder         Used to encode sensitive information.
     * @param sessionRegistry Principal cache.
     */
    protected AbstractClientService(
        R repository,
        PasswordEncoder encoder,
        SessionRegistry sessionRegistry) {
        this.repository = repository;
        this.encoder = encoder;
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Use to authenticate a user against the server.
     *
     * @param username the username identifying the user whose data is required.
     * @return {@link UserDetails} The user details required in order to perform successful authentication.
     * @throws BackendException if failed to load user with HTTP status 500 - Internal Server Error.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<E> user = repository.findByUserName(username);

        if (user.isPresent()) {
            return user.get();
        }

        throw new BackendException("Failed to LOAD user, USERNAME: %s non-existent!".formatted(username), BAD_REQUEST);
    }

    /**
     * Use to update an already existing account or persist a new.
     *
     * @param entity Customer information that is to be persisted/updated into the database.
     * @return {@link Client} The already persisted/updated customer from the database.
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
     * Deletes permanently customer account from the database.
     *
     * @param username Identifier by which customer account has been deleted from database.
     */
    public void delete(String username) {
        if ((repository.deleteByUserName(username) <= 0)) {
            throw new BackendException("Failed to DELETE, USERNAME: %s non-existent!".formatted(username), BAD_REQUEST);
        }
    }

    /**
     * Updates customer account with encoded new password.
     *
     * @param newPassword Replacement for the current password.
     * @param username    Username to query the database.
     * @throws BackendException with 400 BAD REQUEST if fail to change password or 500 INTERNAL SERVER ERROR if another error occurs.
     */
    public void changePassword(String newPassword, String username) {
        try {
            String encoded = encoder.encode(newPassword);
            if (repository.updatePasswordByUsername(username, encoded) > 0) {
                invalidateSession(username);
                return;
            }
            throw new BackendException("Failed to UPDATE password, USERNAME: %s non-existent!".formatted(username), BAD_REQUEST);
        } catch (RuntimeException e) {
            throw new BackendException("Failed to UPDATE password for USERNAME: %s".formatted(username), INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * @param username Username to query the database.
     * @param enabled  Flag used to indicate account state.
     * @throws BackendException with 400 BAD REQUEST if fail to change password or 500 INTERNAL SERVER ERROR if another error occurs.
     */
    public void disableOrEnableByUsername(String username, Boolean enabled) {
        try {
            if (repository.disableOrEnableByUsername(username, enabled) > 0) {
                invalidateSession(username);
                return;
            }
            throw new BackendException("Failed to ENABLE/DISABLE account, USERNAME: %s non-existent!".formatted(username), BAD_REQUEST);
        } catch (RuntimeException e) {
            throw new BackendException("Failed to ENABLE/DISABLE account for USERNAME: %s".formatted(username), INTERNAL_SERVER_ERROR, e);
        }
    }

    private void invalidateSession(String username) {
        sessionRegistry.getAllPrincipals()
                       .stream()
                       .map(Client.class::cast)
                       .filter(client -> Objects.equals(username, client.getUsername()))
                       .map(client -> sessionRegistry.getAllSessions(client, false))
                       .forEach(principalSessions -> principalSessions.forEach(SessionInformation::expireNow));
    }
}

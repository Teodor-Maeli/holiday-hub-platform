package com.platform.rest.assembler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.entity.PlatformClient;
import com.platform.exception.BackendException;
import com.platform.model.PlatformClientRequest;
import com.platform.model.PlatformClientResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>Abstract class that provide common logic for assembling of entities to responses or requests to entities. Encodes password
 * fields.</p>
 * <p>Should implement methods by subclass in order to use it..</p>
 * <p>Since 1.0.</p>
 * <p>RS - response, RQ - request, PC - Platform client</p>
 *
 * @author Teodor Maeli.
 */
public abstract class AbstractClientAssembler
    <RS extends PlatformClientResponse, RQ extends PlatformClientRequest, E extends PlatformClient> {

    private final PasswordEncoder encoder;

    protected AbstractClientAssembler(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Performs full assembly - from request to entity
     *
     * @param entity - Entity data that will be assembled as response.
     * @return {@link PlatformClientResponse} - after successful assembly.
     * @throws BackendException - if the assembly has failed.
     */
    public RS assembly(E entity) {
        try {
            return assemblyInternal(entity, initResponse());
        } catch (RuntimeException e) {
            throw new BackendException("Failed to assembly platform entity to  platform response!", INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Performs full assembly - from request to entity
     *
     * @param request - Request data that will be assembled as entity.
     * @return {@link PlatformClient} - after successful assembly.
     * @throws BackendException - if the assembly has failed.
     */
    public E assembly(RQ request) {
        try {
            return assemblyInternal(request, initEntity());
        } catch (RuntimeException e) {
            throw new BackendException("Failed to assembly platform request to platform client!", INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Used to get properties that needs to be ignored per subclass.
     *
     * @return {@link String[]} after invocation.
     */
    protected String[] getIgnoreProperties() {
        return new String[]{
            "password"
        };
    }

    /**
     * Used to initialize new response instance.
     *
     * @return {@link PlatformClientResponse} after invocation.
     */
    protected abstract RS initResponse();

    /**
     * Used to initialize new entity instance.
     *
     * @return {@link PlatformClient} after invocation.
     */
    protected abstract E initEntity();

    /**
     * Used to assembly common fields, package-private access. Override if behavior not satisfying.
     *
     * @param entity   - Contains data that will be assembled as response.
     * @param response - The already initialized response object that will undergo internal assembly.
     * @return {@link PlatformClientResponse} - after successful post assembly.
     */
    RS assemblyInternal(E entity, RS response) {
        String[] toBeIgnored = getIgnoreProperties();
        BeanUtils.copyProperties(entity, response, toBeIgnored);
        return response;
    }

    /**
     * Used to assembly common fields, package-private access. Override if behavior not satisfying.
     *
     * @param request - Contains data that will be assembled as entity.
     * @param entity  - The already initialized entity object that will undergo internal assembly.
     * @return {@link PlatformClient} after successful post assembly.
     */
    E assemblyInternal(RQ request, E entity) {
        String[] toBeIgnored = getIgnoreProperties();
        BeanUtils.copyProperties(request, entity, toBeIgnored);

        if (request.getPassword() != null) {
            entity.setPassword(encoder.encode(request.getPassword()));
        }

        return entity;
    }
}

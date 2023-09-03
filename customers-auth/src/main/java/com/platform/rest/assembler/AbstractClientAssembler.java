package com.platform.rest.assembler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.entity.Client;
import com.platform.exception.BackendException;
import com.platform.model.ClientRequest;
import com.platform.model.ClientResponse;
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
    <RS extends ClientResponse, RQ extends ClientRequest, E extends Client> {

    private final PasswordEncoder encoder;

    protected AbstractClientAssembler(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Performs full assembly - from request to entity
     *
     * @param entity - Entity data that will be assembled as response.
     * @return {@link ClientResponse} - after successful assembly.
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
     * @return {@link Client} - after successful assembly.
     * @throws BackendException - if the assembly has failed.
     */
    public E assembly(RQ request) {
        try {
            return assemblyInternal(request, initEntity());
        } catch (RuntimeException e) {
            throw new BackendException("Failed to assembly platform request to platform client!", INTERNAL_SERVER_ERROR, e);
        }
    }

    protected String[] getIgnoreProperties() {
        return new String[]{"password"};
    }

    protected abstract RS initResponse();

    protected abstract E initEntity();

    RS assemblyInternal(E entity, RS response) {
        String[] toBeIgnored = getIgnoreProperties();
        BeanUtils.copyProperties(entity, response, toBeIgnored);

        return response;
    }


    E assemblyInternal(RQ request, E entity) {
        String[] toBeIgnored = getIgnoreProperties();
        BeanUtils.copyProperties(request, entity, toBeIgnored);

        if (request.getPassword() != null) {
            entity.setPassword(encoder.encode(request.getPassword()));
        }

        return entity;
    }
}

package com.platform.authorizationserver.handler.impl;

import com.platform.authorizationserver.handler.InvocationHandler;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.dto.ServletRequest;
import com.platform.authorizationserver.model.dto.ServletResponse;
import com.platform.authorizationserver.rest.assembler.LegalEntityAssembler;
import com.platform.authorizationserver.rest.assembler.PersonAssembler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerUpdateInvocationHandler implements InvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;

    @Override
    public ServletResponse handle(ServletRequest request, HandlerAction action) {

        return switch (action) {
            case PERSON_UPDATE -> handlePersonUpdate(request);
            case ENTITY_UPDATE -> handleEntityUpdate(request);
            default ->
                throw new IllegalArgumentException("Could not handle unknown action : " + action);
        };
    }

    private ServletResponse handleEntityUpdate(ServletRequest request) {
        return null;
    }

    private ServletResponse handlePersonUpdate(ServletRequest request) {
        return null;
    }
}

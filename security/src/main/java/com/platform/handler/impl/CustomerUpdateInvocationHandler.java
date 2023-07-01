package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerUpdateInvocationHandler implements InvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;

    @Override
    public ServletResponse handle(ServletRequest request, RequestAction action) {

        return switch (action) {
            case PERSON_UPDATE -> handlePersonUpdate(request);
            case ENTITY_UPDATE -> handleEntityUpdate(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private ServletResponse handleEntityUpdate(ServletRequest request) {
        return null;
    }

    private ServletResponse handlePersonUpdate(ServletRequest request) {
        return null;
    }
}

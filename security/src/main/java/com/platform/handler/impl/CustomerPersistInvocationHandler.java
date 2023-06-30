package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.HandlerAction;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerPersistInvocationHandler implements InvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;

    @Override
    public ServletResponse handle(ServletRequest request, HandlerAction action) {

        return switch (action) {
            case ENTITY_REGISTER -> handleEntityRegister(request);
            case PERSON_REGISTER -> handlePersonRegister(request);
            default ->
                throw new IllegalArgumentException("Could not handle unknown action : " + action);
        };
    }


    private ServletResponse handlePersonRegister(ServletRequest request) {
        return null;
    }


    private ServletResponse handleEntityRegister(ServletRequest request) {
        return null;
    }
}

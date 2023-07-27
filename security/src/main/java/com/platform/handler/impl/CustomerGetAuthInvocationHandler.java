package com.platform.handler.impl;

import com.platform.handler.AuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerGetAuthInvocationHandler implements AuthInvocationHandler {

    private final PersonAssembler personAssembler;
    private final LegalEntityAssembler legalEntityAssembler;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, AuthRequestAction action) {
        return switch (action) {
            case ENTITY_FETCH -> handleEntityFetch(request);
            case PERSON_FETCH -> handlePersonFetch(request);
            default ->
                throw new IllegalArgumentException("Action not supported by " + this.getClass());
        };
    }

    private PlatformServletResponse handlePersonFetch(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntityFetch(PlatformServletRequest request) {
        return null;
    }
}

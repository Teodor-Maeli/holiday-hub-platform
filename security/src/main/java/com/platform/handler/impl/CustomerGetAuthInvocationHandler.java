package com.platform.handler.impl;

import com.platform.handler.AuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;

public class CustomerGetAuthInvocationHandler implements AuthInvocationHandler {

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, AuthRequestAction action) {
        return switch (action) {
            case ENTITY_FETCH -> handleEntityFetch(request);
            case PERSON_FETCH -> handlePersonFetch(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private PlatformServletResponse handlePersonFetch(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntityFetch(PlatformServletRequest request) {
        return null;
    }
}

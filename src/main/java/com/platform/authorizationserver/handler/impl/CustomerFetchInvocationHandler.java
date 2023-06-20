package com.platform.authorizationserver.handler.impl;

import com.platform.authorizationserver.handler.InvocationHandler;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.dto.ServletRequest;
import com.platform.authorizationserver.model.dto.ServletResponse;

public class CustomerFetchInvocationHandler implements InvocationHandler {

    @Override
    public ServletResponse handle(ServletRequest request, HandlerAction action) {
        return switch (action) {
            case ENTITY_FETCH -> handleEntityFetch(request);
            case PERSON_FETCH -> handlePersonFetch(request);
            default ->
                throw new IllegalArgumentException("Could not handle unknown action : " + action);
        };
    }

    private ServletResponse handlePersonFetch(ServletRequest request) {
        return null;
    }

    private ServletResponse handleEntityFetch(ServletRequest request) {
        return null;
    }
}

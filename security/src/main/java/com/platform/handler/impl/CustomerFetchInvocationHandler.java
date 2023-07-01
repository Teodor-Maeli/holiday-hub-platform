package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;

public class CustomerFetchInvocationHandler implements InvocationHandler {

    @Override
    public ServletResponse handle(ServletRequest request, RequestAction action) {
        return switch (action) {
            case ENTITY_FETCH -> handleEntityFetch(request);
            case PERSON_FETCH -> handlePersonFetch(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private ServletResponse handlePersonFetch(ServletRequest request) {
        return null;
    }

    private ServletResponse handleEntityFetch(ServletRequest request) {
        return null;
    }
}

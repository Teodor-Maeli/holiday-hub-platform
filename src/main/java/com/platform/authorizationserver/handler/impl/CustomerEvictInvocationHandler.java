package com.platform.authorizationserver.handler.impl;

import com.platform.authorizationserver.handler.InvocationHandler;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.dto.ServletRequest;
import com.platform.authorizationserver.model.dto.ServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerEvictInvocationHandler implements InvocationHandler {


    @Override
    public ServletResponse handle(ServletRequest request, HandlerAction action) {
        return switch (action) {
            case PERSON_SESSION_INVALIDATE -> handlePersonSessionInvalidate(request);
            case ENTITY_SESSION_INVALIDATE -> handleEntitySessionInvalidate(request);
            default ->
                throw new IllegalArgumentException("Could not handle unknown action : " + action);
        };
    }

    private ServletResponse handlePersonSessionInvalidate(ServletRequest request) {
        return null;
    }

    private ServletResponse handleEntitySessionInvalidate(ServletRequest request) {
        return null;
    }
}

package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerEvictInvocationHandler implements InvocationHandler {


    @Override
    public ServletResponse handle(ServletRequest request, RequestAction action) {
        return switch (action) {
            case PERSON_SESSION_INVALIDATE -> handlePersonSessionInvalidate(request);
            case ENTITY_SESSION_INVALIDATE -> handleEntitySessionInvalidate(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private ServletResponse handlePersonSessionInvalidate(ServletRequest request) {
        return null;
    }

    private ServletResponse handleEntitySessionInvalidate(ServletRequest request) {
        return null;
    }
}

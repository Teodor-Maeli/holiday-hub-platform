package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerEvictInvocationHandler implements InvocationHandler {


    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {
        return switch (action) {
            case PERSON_SESSION_INVALIDATE -> handlePersonSessionInvalidate(request);
            case ENTITY_SESSION_INVALIDATE -> handleEntitySessionInvalidate(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private PlatformServletResponse handlePersonSessionInvalidate(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntitySessionInvalidate(PlatformServletRequest request) {
        return null;
    }
}

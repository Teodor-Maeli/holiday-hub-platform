package com.platform.handler.impl;

import com.platform.handler.AuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerEvictAuthInvocationHandler implements AuthInvocationHandler {


    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, AuthRequestAction action) {
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

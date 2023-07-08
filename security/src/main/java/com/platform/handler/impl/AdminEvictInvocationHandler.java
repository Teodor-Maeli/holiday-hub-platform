package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminEvictInvocationHandler implements InvocationHandler {


    SessionRegistry sessionRegistry;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {
        return switch (action) {
            case ADMIN_ENTITY_SESSION_INVALIDATE -> handleEntitySessionInvalidate(request);
            case ADMIN_PERSON_SESSION_INVALIDATE -> handlePersonSessionInvalidate(request);
            case ADMIN_ENTITY_DELETE -> handleEntityDelete(request);
            case ADMIN_PERSON_DELETE -> handlePersonDelete(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private PlatformServletResponse handlePersonDelete(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntityDelete(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handlePersonSessionInvalidate(PlatformServletRequest request) {
        PersonRequest platformClientRequest = (PersonRequest) request.getPlatformClientRequest();
        sessionRegistry.getAllPrincipals();
        return null;
    }

    private PlatformServletResponse handleEntitySessionInvalidate(PlatformServletRequest request) {
        return null;
    }
}

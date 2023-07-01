package com.platform.handler.impl;

import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminEvictInvocationHandler implements InvocationHandler {


    SessionRegistry sessionRegistry;

    @Override
    public ServletResponse handle(ServletRequest request, RequestAction action) {
        return switch (action) {
            case ADMIN_ENTITY_SESSION_INVALIDATE -> handleEntitySessionInvalidate(request);
            case ADMIN_PERSON_SESSION_INVALIDATE -> handlePersonSessionInvalidate(request);
            case ADMIN_ENTITY_DELETE -> handleEntityDelete(request);
            case ADMIN_PERSON_DELETE -> handlePersonDelete(request);
            default ->
                throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private ServletResponse handlePersonDelete(ServletRequest request) {
        return null;
    }

    private ServletResponse handleEntityDelete(ServletRequest request) {
        return null;
    }

    private ServletResponse handlePersonSessionInvalidate(ServletRequest request) {
        PersonRequest platformClientRequest = (PersonRequest) request.getPlatformClientRequest();
        sessionRegistry.getAllPrincipals();
        return null;
    }

    private ServletResponse handleEntitySessionInvalidate(ServletRequest request) {
        return null;
    }
}

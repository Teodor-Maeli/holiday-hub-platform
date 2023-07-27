package com.platform.handler.impl;

import com.platform.handler.AuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.dto.LegalEntityRequest;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerEvictAuthInvocationHandler implements AuthInvocationHandler {

    private final SessionRegistry sessionRegistry;

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
        PersonRequest personRequest = (PersonRequest) request.getPlatformClientRequest();
        String username = personRequest.getPersonUsername();
        Assert.notNull(username, "Username cannot be null when trying to invalidate session!");
        return evictSessionByUserName(username);
    }

    private PlatformServletResponse handleEntitySessionInvalidate(PlatformServletRequest request) {
        LegalEntityRequest entityRequest = (LegalEntityRequest) request.getPlatformClientRequest();
        String username = entityRequest.getCompanyUserName();
        Assert.notNull(username, "Username cannot be null when trying to invalidate session!");
        return evictSessionByUserName(username);
    }

    private PlatformServletResponse evictSessionByUserName(String username) {
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(username, false);
        allSessions.forEach(SessionInformation::expireNow);
        return new PlatformServletResponse();
    }
}

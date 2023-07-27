package com.platform.handler.impl;

import com.platform.domain.entity.LegalEntity;
import com.platform.domain.repository.LegalEntityRepository;
import com.platform.domain.repository.PersonRepository;
import com.platform.handler.AuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.dto.LegalEntityRequest;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class AdminEvictAuthInvocationHandler implements AuthInvocationHandler {

    private SessionRegistry sessionRegistry;
    private PersonRepository personRepository;
    private LegalEntityRepository legalEntityRepository;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, AuthRequestAction action) {
        return switch (action) {
            case ADMIN_ENTITY_SESSION_INVALIDATE -> handleEntitySessionInvalidate(request);
            case ADMIN_PERSON_SESSION_INVALIDATE -> handlePersonSessionInvalidate(request);
            case ADMIN_ENTITY_DELETE -> handleEntityDelete(request);
            case ADMIN_PERSON_DELETE -> handlePersonDelete(request);
            default -> throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private PlatformServletResponse handlePersonDelete(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntityDelete(PlatformServletRequest request) {
        return null;
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

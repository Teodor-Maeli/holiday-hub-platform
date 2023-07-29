package com.platform.handler.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.repository.LegalEntityRepository;
import com.platform.domain.repository.PersonRepository;
import com.platform.exception.SecurityException;
import com.platform.handler.SecurityInvocationHandler;
import com.platform.model.RequestAction;
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
public class AdminEvictHandler implements SecurityInvocationHandler {

    private SessionRegistry sessionRegistry;
    private PersonRepository personRepository;
    private LegalEntityRepository legalEntityRepository;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case ADMIN_ENTITY_SESSION_INVALIDATE:
                return handleEntitySessionInvalidate(request);
            case ADMIN_PERSON_SESSION_INVALIDATE:
                return handlePersonSessionInvalidate(request);
            case ADMIN_ENTITY_DELETE:
                return handleEntityDelete(request);
            case ADMIN_PERSON_DELETE:
                return handlePersonDelete(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .action(action)
                                       .message("Could not handle the requested action!")
                                       .build();
        }
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

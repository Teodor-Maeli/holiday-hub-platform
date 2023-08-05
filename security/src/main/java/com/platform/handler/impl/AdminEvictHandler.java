package com.platform.handler.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.util.Assert.notNull;

import com.platform.exception.SecurityException;
import com.platform.handler.SecurityInvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminEvictHandler implements SecurityInvocationHandler<Void> {

    private SessionRegistry sessionRegistry;

    @Override
    public Void handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case ADMIN_SESSION_INVALIDATE:
                return sessionInvalidate(request);
            case ADMIN_ENTITY_DELETE:
                return handleEntityDelete(request);
            case ADMIN_PERSON_DELETE:
                return handlePersonDelete(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .message("Could not handle the requested action!")
                                       .build();
        }
    }

    private Void handlePersonDelete(PlatformServletRequest request) {
        return null;
    }

    private Void handleEntityDelete(PlatformServletRequest request) {
        return null;
    }



    @Override
    public void validate(PlatformServletRequest request) {
        notNull(request, "Request cannot be null!");
        notNull(request.getPlatformClientRequest(), "PlatformClientRequest cannot be null!");
        notNull(request.getPlatformClientRequest().getUsername(), "Username cannot be null!");
    }

    private Void sessionInvalidate(PlatformServletRequest request) {
        validate(request);
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(request.getPlatformClientRequest().getUsername(), false);
        allSessions.forEach(SessionInformation::expireNow);
        return null;
    }
}

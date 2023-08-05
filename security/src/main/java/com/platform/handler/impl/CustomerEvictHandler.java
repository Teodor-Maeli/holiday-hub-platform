package com.platform.handler.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.util.Assert.notNull;

import com.platform.exception.SecurityException;
import com.platform.handler.SecurityInvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerEvictHandler implements SecurityInvocationHandler<Void> {

    private final SessionRegistry sessionRegistry;

    @Override
    public Void handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case SESSION_INVALIDATE:
                return sessionInvalidate(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .message("Could not handle the requested action!")
                                       .build();
        }
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

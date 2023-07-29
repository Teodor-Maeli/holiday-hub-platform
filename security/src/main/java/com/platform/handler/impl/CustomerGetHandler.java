package com.platform.handler.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.exception.SecurityException;
import com.platform.handler.SecurityInvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerGetHandler implements SecurityInvocationHandler {

    private final PersonAssembler personAssembler;
    private final LegalEntityAssembler legalEntityAssembler;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case ENTITY_FETCH:
                return handleEntityFetch(request);
            case PERSON_FETCH:
                return handlePersonFetch(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .action(action)
                                       .message("Could not handle the requested action!")
                                       .build();
        }
    }

    private PlatformServletResponse handlePersonFetch(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntityFetch(PlatformServletRequest request) {
        return null;
    }
}

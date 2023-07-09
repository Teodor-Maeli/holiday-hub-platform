package com.platform.handler.impl;

import com.platform.domain.entity.Person;
import com.platform.domain.repository.LegalEntityRepository;
import com.platform.domain.repository.PersonRepository;
import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerUpdateInvocationHandler implements InvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;
    private PersonRepository personRepository;
    private LegalEntityRepository legalEntityRepository;



    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        return switch (action) {
            case PERSON_UPDATE -> handlePersonUpdate(request);
            case ENTITY_UPDATE -> handleEntityUpdate(request);
            default -> throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }

    private PlatformServletResponse handleEntityUpdate(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handlePersonUpdate(PlatformServletRequest request) {
        return null;
    }
}

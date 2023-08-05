package com.platform.handler.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.entity.LegalEntity;
import com.platform.domain.entity.Person;
import com.platform.domain.repository.LegalEntityRepository;
import com.platform.domain.repository.PersonRepository;
import com.platform.exception.SecurityException;
import com.platform.handler.SecurityInvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.LegalEntityRequest;
import com.platform.model.dto.PersonRequest;
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
public class CustomerUpdateHandler implements SecurityInvocationHandler<PlatformServletResponse> {

    private final PersonAssembler personAssembler;
    private final LegalEntityAssembler legalEntityAssembler;
    private final PersonRepository personRepository;
    private final LegalEntityRepository legalEntityRepository;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case PERSON_UPDATE:
                return handlePersonUpdate(request);
            case ENTITY_UPDATE:
                return handleEntityUpdate(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .message("Could not handle the requested action!")
                                       .build();
        }
    }

    @Override
    public void validate(PlatformServletRequest request) {

    }

    private PlatformServletResponse handleEntityUpdate(PlatformServletRequest request) {
        LegalEntityRequest platformClientRequest = (LegalEntityRequest) request.getPlatformClientRequest();
        LegalEntity legalEntity = legalEntityAssembler.assemble(platformClientRequest);
        legalEntityRepository.save(legalEntity);
        return new PlatformServletResponse();
    }

    private PlatformServletResponse handlePersonUpdate(PlatformServletRequest request) {
        PersonRequest personRequest = (PersonRequest) request.getPlatformClientRequest();
        Person person = personAssembler.assemble(personRequest);
        personRepository.save(person);
        return new PlatformServletResponse();
    }
}

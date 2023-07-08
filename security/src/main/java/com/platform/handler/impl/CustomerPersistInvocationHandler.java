package com.platform.handler.impl;

import com.platform.domain.entity.LegalEntity;
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
public class CustomerPersistInvocationHandler implements InvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;
    private PersonRepository personRepository;
    private LegalEntityRepository legalEntityRepository;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        return switch (action) {
            case ENTITY_REGISTER -> entityRegister(request);
            case PERSON_REGISTER -> personRegister(request);
            default -> throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }


    private PlatformServletResponse personRegister(PlatformServletRequest request) {
        Person person = personAssembler.assemble(request.getPlatformClientRequest());
        personRepository.save(person);
        log.info("Successfully persisted type {} with username {}", person.getClass(), person.getUsername());

        PlatformServletResponse response = new PlatformServletResponse();
        response.setPlatformClientResponse(personAssembler.assemble(person));
        return response;
    }

    private PlatformServletResponse entityRegister(PlatformServletRequest request) {
        LegalEntity legalEntity =
            legalEntityRepository.save(legalEntityAssembler.assemble(request.getPlatformClientRequest()));
        log.info("Successfully persisted type {} with username {} and company {}",
            legalEntity.getClass(), legalEntity.getUsername(), legalEntity.getCompanyName());

        PlatformServletResponse response = new PlatformServletResponse();
        response.setPlatformClientResponse(personAssembler.assemble(legalEntity));
        return response;
    }
}

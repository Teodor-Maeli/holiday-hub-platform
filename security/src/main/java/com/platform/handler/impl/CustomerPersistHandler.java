package com.platform.handler.impl;

import static com.platform.model.RequestAction.ENTITY_LOGIN;
import static com.platform.model.RequestAction.PERSON_LOGIN;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.platform.domain.entity.LegalEntity;
import com.platform.domain.entity.Person;
import com.platform.domain.entity.PlatformClient;
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
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerPersistHandler implements SecurityInvocationHandler {

    private final PersonAssembler personAssembler;
    private final LegalEntityAssembler legalEntityAssembler;
    private final PersonRepository personRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final PasswordEncoder encoder;
    private final SecurityContextRepository securityContextRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final SessionRegistry sessionRegistry;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case ENTITY_REGISTER:
                return entityRegister(request);
            case PERSON_REGISTER:
                return personRegister(request);
            case ENTITY_LOGIN:
                return handleEntityLogin(request);
            case PERSON_LOGIN:
                return handlePersonLogin(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .action(action)
                                       .message("Could not handle the requested action!")
                                       .build();
        }
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

    private PlatformServletResponse handlePersonLogin(PlatformServletRequest request) {
        PersonRequest personRequest = (PersonRequest) request.getPlatformClientRequest();
        String username = personRequest.getPersonUsername();
        String rawPassword = personRequest.getPassword();

        Assert.notNull(username, "PERSON USERNAME required, unable to register session");
        Assert.notNull(rawPassword, "PERSON PASSWORD  required, unable to register session");

        Person person = personRepository.findByUserName(username)
                                        .orElseThrow(securityException(username, PERSON_LOGIN));

        return authenticate(username, rawPassword, person, personRepository, PERSON_LOGIN);
    }


    private PlatformServletResponse handleEntityLogin(PlatformServletRequest request) {
        LegalEntityRequest legalEntityRequest = (LegalEntityRequest) request.getPlatformClientRequest();
        String username = legalEntityRequest.getCompanyUserName();
        String rawPassword = legalEntityRequest.getPassword();

        Assert.notNull(username, "LEGAL ENTITY USERNAME required, unable to register session");
        Assert.notNull(rawPassword, "LEGAL ENTITY PASSWORD  required, unable to register session");

        LegalEntity legalEntity = legalEntityRepository.findByUserName(username)
                                                       .orElseThrow(securityException(username, ENTITY_LOGIN));

        return authenticate(username, rawPassword, legalEntity, legalEntityRepository, ENTITY_LOGIN);
    }

    private <T extends PlatformClient, I extends Number> PlatformServletResponse authenticate(String clientUsername,
                                                                                              String clientRawPassword,
                                                                                              T platformClient,
                                                                                              JpaRepository<T, I> clientRepository,
                                                                                              RequestAction action) {
        if (passwordMismatches(clientRawPassword, platformClient)) {
            throw SecurityException.builder()
                                   .httpStatus(FORBIDDEN)
                                   .action(action)
                                   .message("Incorrect credentials!")
                                   .build();
        }

        String sessionId = httpServletRequest.getSession().getId();
        SecurityContext context = SecurityContextHolder.getContext();
        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(platformClient.getUsername(),
                                                                                        platformClient.getPassword(),
                                                                                        platformClient.getAuthorities());
        context.setAuthentication(authToken);
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        sessionRegistry.registerNewSession(sessionId, authToken.getPrincipal());

        logSession(clientUsername, platformClient, clientRepository, action, sessionId);

        return new PlatformServletResponse();
    }

    private <T extends PlatformClient, I extends Number> void logSession(String clientUsername, T platformClient,
                                                                         JpaRepository<T, I> clientRepository, RequestAction action,
                                                                         String sessionId) {
        try {
            platformClient.setMostRecentSessionInitiatedDate(LocalDateTime.now());
            platformClient.setMostRecentSessionId(sessionId);
            clientRepository.save(platformClient);
        } catch (PersistenceException pe) {
            sessionRegistry.getSessionInformation(sessionId).expireNow();
            throw SecurityException.builder()
                                   .message("Error occurred while persisting session info for username :" + clientUsername)
                                   .action(action)
                                   .throwable(pe)
                                   .build();
        }
    }

    private <T extends PlatformClient> boolean passwordMismatches(String clientRawPassword, T platformClient) {
        return !encoder.matches(clientRawPassword, platformClient.getPassword());
    }

    private Supplier<SecurityException> securityException(String username, RequestAction action) {
        return () -> SecurityException.builder()
                                      .httpStatus(FORBIDDEN)
                                      .action(action)
                                      .message("Client " + username + " non-existent!")
                                      .build();
    }
}

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
import com.platform.rest.assembler.Assembler;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerPersistHandler implements SecurityInvocationHandler<Void> {

    private final PersonAssembler personAssembler;
    private final LegalEntityAssembler entityAssembler;
    private final PersonRepository personRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final PasswordEncoder encoder;
    private final SecurityContextRepository securityContextRepository;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final SessionRegistry sessionRegistry;

    @Override
    public Void handle(PlatformServletRequest request, RequestAction action) {

        switch (action) {
            case ENTITY_REGISTER:
                return register(request, legalEntityRepository, entityAssembler);
            case PERSON_REGISTER:
                return register(request, personRepository, personAssembler);
            case ENTITY_LOGIN:
                return entityLogin(request);
            case PERSON_LOGIN:
                return personLogin(request);
            default:
                throw SecurityException.builder()
                                       .httpStatus(INTERNAL_SERVER_ERROR)
                                       .message("Could not handle the requested action: " + action)
                                       .build();
        }
    }

    @Override
    public void validate(PlatformServletRequest request) {
        //TODO Improve or divide validation for both flows.
        requireNonNull(request, "PlatformServletRequest cannot be null!");
        requireNonNull(request.getPlatformClientRequest(), "PlatformClientRequest cannot be null!");
        requireNonNull(request.getPlatformClientRequest().getUsername(), "Username cannot be null!");
        requireNonNull(request.getPlatformClientRequest().getPassword(), "Password cannot be null!");
    }


    private <T1 extends PlatformClient, T2 extends Number, T3 extends Assembler> Void register(PlatformServletRequest request,
                                                                                               JpaRepository<T1, T2> repository,
                                                                                               T3 assembler) {
        validate(request);
        PlatformClient client = assembler.assemble(request.getPlatformClientRequest());
        repository.save((T1) client);
        return null;
    }

    private Void personLogin(PlatformServletRequest request) {
        PersonRequest personRequest = (PersonRequest) request.getPlatformClientRequest();
        String username = personRequest.getUsername();
        String rawPassword = personRequest.getPassword();

        validate(request);

        Person person = personRepository.findByUserName(username)
                                        .orElseThrow(securityException(username, PERSON_LOGIN));

        authenticate(username, rawPassword, person, personRepository, PERSON_LOGIN);
        return null;
    }

    private Void entityLogin(PlatformServletRequest request) {
        validate(request);

        LegalEntityRequest legalEntityRequest = (LegalEntityRequest) request.getPlatformClientRequest();
        String username = legalEntityRequest.getUsername();
        String rawPassword = legalEntityRequest.getPassword();

        LegalEntity legalEntity = legalEntityRepository.findByUserName(username)
                                                       .orElseThrow(securityException(username, ENTITY_LOGIN));

        authenticate(username, rawPassword, legalEntity, legalEntityRepository, ENTITY_LOGIN);
        return null;
    }

    private <T1 extends PlatformClient, T2 extends Number> void authenticate(String clientUsername,
                                                                             String clientRawPassword,
                                                                             T1 platformClient,
                                                                             JpaRepository<T1, T2> clientRepository,
                                                                             RequestAction action) {
        if (passwordMismatches(clientRawPassword, platformClient)) {
            throw SecurityException.builder()
                                   .httpStatus(FORBIDDEN)
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

        logSession(clientUsername,
                   platformClient,
                   clientRepository,
                   action,
                   sessionId);
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
                                   .message("Error occurred while persisting session info for username: " + clientUsername)
                                   .cause(pe)
                                   .build();
        }
    }

    private <T extends PlatformClient> boolean passwordMismatches(String rawPassword, T platformClient) {
        return !encoder.matches(rawPassword, platformClient.getPassword());
    }

    private Supplier<SecurityException> securityException(String username, RequestAction action) {
        return () -> SecurityException.builder()
                                      .httpStatus(FORBIDDEN)
                                      .message("Client " + username + " non-existent!")
                                      .build();
    }
}

package com.platform.handler.impl;

import com.platform.domain.entity.LegalEntity;
import com.platform.domain.entity.Person;
import com.platform.domain.entity.PlatformClient;
import com.platform.domain.repository.LegalEntityRepository;
import com.platform.domain.repository.PersonRepository;
import com.platform.handler.AuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
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
@AllArgsConstructor
public class CustomerPersistAuthInvocationHandler implements AuthInvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;
    private PersonRepository personRepository;
    private LegalEntityRepository legalEntityRepository;
    private PasswordEncoder encoder;
    private SecurityContextRepository securityContextRepository;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private SessionRegistry sessionRegistry;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, AuthRequestAction action) {

        return switch (action) {
            case ENTITY_REGISTER -> entityRegister(request);
            case PERSON_REGISTER -> personRegister(request);
            case ENTITY_LOGIN -> handleEntityLogin(request);
            case PERSON_LOGIN -> handlePersonLogin(request);
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

    private PlatformServletResponse handlePersonLogin(PlatformServletRequest request) {
        PersonRequest personRequest = (PersonRequest) request.getPlatformClientRequest();
        String username = personRequest.getPersonUsername();
        String rawPassword = personRequest.getPassword();

        Assert.notNull(username, "USERNAME required, unable to register session");
        Assert.notNull(rawPassword, "PASSWORD  required, unable to register session");

        Person person = personRepository.findByUserName(username)
            .orElseThrow(() -> new IllegalArgumentException("Client " + username + " non-existent!"));

        return authenticate(username, rawPassword, person, personRepository);
    }

    private PlatformServletResponse handleEntityLogin(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse authenticate(String username,
        String rawPassword, PlatformClient client, JpaRepository repository) {

        if (!encoder.matches(rawPassword, client.getPassword())) {
            throw new AuthenticationServiceException("Incorrect credentials!");
        }

        String sessionId = httpServletRequest.getSession().getId();
        SecurityContext context = SecurityContextHolder.getContext();
        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(client.getUsername(),
            client.getPassword(), client.getAuthorities());
        context.setAuthentication(authToken);
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        sessionRegistry.registerNewSession(sessionId, authToken.getPrincipal());

        try {
            client.setMostRecentSessionInitiatedDate(LocalDateTime.now());
            client.setMostRecentSessionId(sessionId);
            repository.save(client);
        } catch (PersistenceException pe) {
            sessionRegistry.getSessionInformation(sessionId).expireNow();
            throw new AuthenticationServiceException(
                "Error occurred while updating sessionInfo for username :" + username, pe);
        }

        return new PlatformServletResponse();
    }

}

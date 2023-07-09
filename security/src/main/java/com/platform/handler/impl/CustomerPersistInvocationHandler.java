package com.platform.handler.impl;

import com.platform.domain.entity.LegalEntity;
import com.platform.domain.entity.Person;
import com.platform.domain.entity.PlatformClient;
import com.platform.domain.repository.LegalEntityRepository;
import com.platform.domain.repository.PersonRepository;
import com.platform.handler.InvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import com.platform.rest.assembler.LegalEntityAssembler;
import com.platform.rest.assembler.PersonAssembler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class CustomerPersistInvocationHandler implements InvocationHandler {

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
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

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

        Assert.notNull(username, "USERNAME cannot be null when trying to login and register a session!");
        Assert.notNull(rawPassword, "PASSWORD cannot be null when trying to login and register a session!");

        Person person = personRepository.findByUserName(username)
            .orElseThrow(() -> new IllegalArgumentException("Client " + username + " non-existent!"));

        if (!encoder.matches(rawPassword, person.getPassword())) {
            throw new IllegalArgumentException("Incorrect credentials!");
        }

        authenticateAndRegisterNewSession(person);

        return new PlatformServletResponse();
    }

    private void authenticateAndRegisterNewSession(PlatformClient client) {
        SecurityContext context = SecurityContextHolder.getContext();
        AbstractAuthenticationToken abstractAuthenticationToken =
            new UsernamePasswordAuthenticationToken(client.getUsername(), client.getPassword(), client.getAuthorities());
        context.setAuthentication(abstractAuthenticationToken);
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);
        sessionRegistry.registerNewSession(httpServletRequest.getSession().getId(), abstractAuthenticationToken.getPrincipal());
    }

    private PlatformServletResponse handleEntityLogin(PlatformServletRequest request) {
        return null;
    }

}

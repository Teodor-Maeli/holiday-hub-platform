package com.platform.handler.impl;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerUpdateInvocationHandler implements InvocationHandler {

    private PersonAssembler personAssembler;
    private LegalEntityAssembler legalEntityAssembler;
    private PersonRepository personRepository;
    private LegalEntityRepository legalEntityRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder encoder;
    private SessionRegistry sessionRegistry;

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {

        return switch (action) {
            case PERSON_UPDATE -> handlePersonUpdate(request);
            case ENTITY_UPDATE -> handleEntityUpdate(request);
            case ENTITY_LOGIN -> handleEntityLogin(request);
            case PERSON_LOGIN -> handlePersonLogin(request);
            default -> throw new IllegalArgumentException("Could not handle following action : " + action);
        };
    }


    private PlatformServletResponse handlePersonLogin(PlatformServletRequest request) {
        String username = request.getPlatformClientRequest().getUsername();
        Person person = personRepository.findByUserName(username)
            .orElseThrow(() -> new IllegalArgumentException("Client " + username + " non-existent!"));
        String encodedPassword = person.getPassword();
        String rawPassword = request.getPlatformClientRequest().getPassword();

        if (encoder.matches(rawPassword,encodedPassword)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, encodedPassword);
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                requestAttributes.getRequest().setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                return new PlatformServletResponse(); //TODO need to implement proper response
            }

            throw new IllegalArgumentException("Could not set session!");
        }
        throw new IllegalArgumentException("Incorrect credentials!");
    }

    private PlatformServletResponse handleEntityLogin(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handleEntityUpdate(PlatformServletRequest request) {
        return null;
    }

    private PlatformServletResponse handlePersonUpdate(PlatformServletRequest request) {
        return null;
    }
}

package com.platform.authorizationserver.rest.assembler;

import com.platform.authorizationserver.domain.entity.Person;
import com.platform.authorizationserver.domain.entity.PlatformClient;
import com.platform.authorizationserver.model.Role;
import com.platform.authorizationserver.model.dto.PersonRequest;
import com.platform.authorizationserver.model.dto.PersonResponse;
import com.platform.authorizationserver.model.dto.PlatformClientRequest;
import com.platform.authorizationserver.model.dto.PlatformClientResponse;
import java.util.stream.Collectors;

public class PersonAssembler implements Assembler {

    @Override
    public PlatformClientResponse assemble(PlatformClient entity) {
        Person person = (Person) entity;

        return PersonResponse.builder()
            .username(person.getUsername())
            .isAccountNonExpired(person.isAccountNonExpired())
            .isAccountNonLocked(person.isAccountNonLocked())
            .isCredentialsNonExpired(person.isCredentialsNonExpired())
            .roles(person.getAuthorities().stream()
                .map(grantedAuthority -> Role.valueOf(grantedAuthority.getAuthority()))
                .collect(Collectors.toSet()))
            .isEnabled(person.isEnabled())
            .build();
    }

    @Override
    public PlatformClient assemble(PlatformClientRequest request) {
        PersonRequest personRequest = (PersonRequest) request;

        return Person.builder()
            .username(personRequest.getUsername())
            .password(personRequest.getPassword())
            .roles(personRequest.getRoles())
            .isAccountNonExpired(personRequest.isAccountNonExpired())
            .isCredentialsNonExpired(personRequest.isCredentialsNonExpired())
            .isAccountNonLocked(personRequest.isAccountNonLocked())
            .isEnabled(personRequest.isEnabled())
            .roles(personRequest.getRoles())
            .build();
    }
}

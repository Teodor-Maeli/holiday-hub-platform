package com.platform.rest.assembler;

import com.platform.domain.entity.Person;
import com.platform.domain.entity.PlatformClient;
import com.platform.model.dto.PersonRequest;
import com.platform.model.dto.PersonResponse;
import com.platform.model.dto.PlatformClientRequest;
import com.platform.model.dto.PlatformClientResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PersonAssembler implements Assembler {

    private PasswordEncoder encoder;

    @Override
    public PlatformClientResponse assemble(PlatformClient entity) {
        Person person = (Person) entity;
        PersonResponse personResponse = new PersonResponse();

        personResponse.setUsername(person.getUsername());
        personResponse.setRoles(person.getRoles());
        personResponse.setIsEnabled(person.isEnabled());
        personResponse.setBirthDate(person.getBirthDate());
        personResponse.setFamilyName(person.getFamilyName());
        personResponse.setGivenName(person.getGivenName());
        personResponse.setMiddleName(person.getMiddleName());
        personResponse.setSubscriptionEnds(person.getSubscriptionEnds());
        personResponse.setSubscriptionStarts(person.getSubscriptionStarts());
        personResponse.setIsEnabled(person.getEnabled());
        personResponse.setMostRecentSessionId(person.getMostRecentSessionId());
        personResponse.setMostRecentSessionInitiatedDate(person.getMostRecentSessionInitiatedDate());
        personResponse.setRegisteredDate(person.getRegisteredDate());

        return personResponse;
    }

    @Override
    public Person assemble(PlatformClientRequest request) {
        PersonRequest personRequest = (PersonRequest) request;
        Person person = new Person();

        person.setFamilyName(personRequest.getFamilyName());
        person.setGivenName(personRequest.getGivenName());
        person.setMiddleName(personRequest.getMiddleName());
        person.setUsername(personRequest.getPersonUsername());
        person.setPassword(encoder.encode(personRequest.getPassword()));
        person.setRoles(personRequest.getRoles());
        person.setEnabled(personRequest.getIsEnabled());
        person.setPremium(personRequest.getIsPremiumEnabled());
        person.setSubscriptionStarts(personRequest.getSubscriptionStarts());
        person.setSubscriptionEnds(personRequest.getSubscriptionEnds());

        return person;
    }
}

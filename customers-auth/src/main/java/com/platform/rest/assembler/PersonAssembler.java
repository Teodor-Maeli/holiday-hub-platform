package com.platform.rest.assembler;

import com.platform.domain.entity.Person;
import com.platform.model.PersonRequest;
import com.platform.model.PersonResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PersonAssembler extends AbstractClientAssembler<PersonResponse, PersonRequest, Person> {

    protected PersonAssembler(PasswordEncoder encoder) {
        super(encoder);
    }

    @Override
    protected PersonResponse initResponse() {
        return new PersonResponse();
    }

    @Override
    protected Person initEntity() {
        return new Person();
    }
}

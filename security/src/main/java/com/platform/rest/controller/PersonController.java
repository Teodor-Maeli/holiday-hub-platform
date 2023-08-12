package com.platform.rest.controller;

import com.platform.aspect.annotation.IOLogger;
import com.platform.model.PersonRequest;
import com.platform.model.PersonResponse;
import com.platform.rest.assembler.PersonAssembler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth/v1/person")
public class PersonController {

    private final PersonAssembler assembler;

    public PersonController(PersonAssembler assembler) {
        this.assembler = assembler;
    }

    @IOLogger
    @PostMapping(path = "/register")
    public PersonResponse register(@RequestBody PersonRequest request) {
        return null;
    }

}

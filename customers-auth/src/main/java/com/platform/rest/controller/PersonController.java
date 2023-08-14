package com.platform.rest.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.platform.aspect.logger.IOLogger;
import com.platform.domain.entity.Person;
import com.platform.model.PersonRequest;
import com.platform.model.PersonResponse;
import com.platform.rest.assembler.PersonAssembler;
import com.platform.service.PersonService;
import jakarta.websocket.server.PathParam;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/customers-auth/v1/person",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class PersonController {

    private final PersonAssembler assembler;
    private final PersonService service;

    public PersonController(PersonAssembler assembler, PersonService service) {
        this.assembler = assembler;
        this.service = service;
    }

    @IOLogger
    @PostMapping(path = "/register")
    public ResponseEntity<PersonResponse> register(@RequestBody PersonRequest request) {
        Person entity = service.save(assembler.assembly(request));
        return ResponseEntity
            .status(CREATED)
            .body(assembler.assembly(entity));
    }

    @IOLogger
    @GetMapping(path = "/get/{username}")
    public ResponseEntity<PersonResponse> getByUsername(@PathVariable("username") String username) {
        Person entity = service.getByUsername(username);
        return ResponseEntity
            .status(OK)
            .body(assembler.assembly(entity));
    }

    @IOLogger
    @PatchMapping(path = "/update/password")
    public ResponseEntity<Void> updatePassword(@RequestHeader String password,
                                               @RequestHeader String username) {
        service.changePassword(password, username);
        return ResponseEntity
            .noContent()
            .build();
    }

    @IOLogger
    @PatchMapping(path = "/update/{username}")
    public ResponseEntity<Void> disableOrEnableAccount(@PathParam("username") String username,
                                                       @Param("enabled") Boolean enabled) {
        service.disableOrEnableByUsername(username, enabled);
        return ResponseEntity
            .noContent()
            .build();
    }
}

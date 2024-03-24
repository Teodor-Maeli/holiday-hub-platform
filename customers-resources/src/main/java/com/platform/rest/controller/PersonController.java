package com.platform.rest.controller;

import com.platform.aspect.annotation.IOLogger;
import com.platform.mapper.PersonMapper;
import com.platform.model.Person;
import com.platform.model.PersonRegistration;
import com.platform.persistence.entity.PersonEntity;
import com.platform.service.ClientService;
import com.platform.service.decorator.DecoratingOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(
    path = PersonController.CUSTOMERS_AUTH_V_1_PERSON,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

  public static final String CUSTOMERS_AUTH_V_1_PERSON = "/customers-auth/v1/person";

  private final PersonMapper mapper;

  private final ClientService<PersonEntity> service;

  public PersonController(PersonMapper mapper, ClientService<PersonEntity> service) {
    this.mapper = mapper;
    this.service = service;
  }

  @IOLogger
  @PostMapping(path = "/register")
  public ResponseEntity<Person> register(@RequestBody PersonRegistration request) {
    PersonEntity entity = service.save(mapper.toEntity(request));
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @GetMapping(path = "/get/{clientUsername}")
  @PreAuthorize("#clientUsername == authentication.principal")
  public ResponseEntity<Person> getByUsername(@RequestParam("include") Set<DecoratingOptions> aggregations,
                                              @PathVariable("clientUsername") String clientUsername) {
    PersonEntity entity = service.loadUserByUsernameDecorated(aggregations, clientUsername);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @PatchMapping(path = "/update/password")
  @PreAuthorize("#clientUsername == authentication.principal")
  public ResponseEntity<Void> updatePassword(@RequestHeader String password,
                                             @RequestHeader String username) {
    service.changePassword(password, username);
    return ResponseEntity
        .noContent()
        .build();
  }
}

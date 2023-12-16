package com.platform.rest.controller;

import com.platform.aspect.logger.IOLogger;
import com.platform.domain.entity.Person;
import com.platform.rest.mapper.PersonMapper;
import com.platform.rest.resource.PersonRequest;
import com.platform.rest.resource.PersonResponse;
import com.platform.service.PersonService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(
    path = PersonController.CUSTOMERS_AUTH_V_1_PERSON,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

  public static final String CUSTOMERS_AUTH_V_1_PERSON = "/customers-auth/v1/person";

  private final PersonMapper mapper;

  private final PersonService service;

  public PersonController(PersonMapper mapper, PersonService service) {
    this.mapper = mapper;
    this.service = service;
  }

  @IOLogger
  @PostMapping(path = "/register")
  public ResponseEntity<PersonResponse> register(@RequestBody PersonRequest request) {
    Person entity = service.save(mapper.toEntity(request));
    return ResponseEntity
        .status(CREATED)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @GetMapping(path = "/get/{username}")
  public ResponseEntity<PersonResponse> getByUsername(@PathVariable("username") String username) {
    Person entity = (Person) service.loadUserByUsername(username);
    return ResponseEntity
        .status(OK)
        .body(mapper.toResponse(entity));
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
  public ResponseEntity<Void> disableOrEnableAccount(@PathVariable("username") String username,
                                                     @Param("enabled") Boolean enabled) {
    service.disableOrEnableByUsername(username, enabled);
    return ResponseEntity
        .noContent()
        .build();
  }
}

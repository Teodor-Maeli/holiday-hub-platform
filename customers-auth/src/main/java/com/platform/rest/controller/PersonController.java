package com.platform.rest.controller;

import com.platform.aspect.logger.IOLogger;
import com.platform.common.model.AggregationOptions;
import com.platform.domain.entity.Person;
import com.platform.rest.mapper.PersonMapper;
import com.platform.rest.resource.PersonRegistration;
import com.platform.rest.resource.PersonResponse;
import com.platform.service.PersonService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
  public ResponseEntity<PersonResponse> register(@RequestBody PersonRegistration request) {
    Person entity = service.save(mapper.toEntity(request));
    return ResponseEntity
        .status(CREATED)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @GetMapping(path = "/get/{clientUsername}")
  public ResponseEntity<PersonResponse> getByUsername(@RequestParam("include") Set<AggregationOptions> aggregations,
                                                      @PathVariable("clientUsername") String clientUsername) {
    Person entity = service.loadUserByUsernameAggregated(aggregations, clientUsername);
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
}

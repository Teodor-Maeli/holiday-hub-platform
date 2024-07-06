package com.platform.rest.controller;

import com.platform.aspect.audit.Audited;
import com.platform.mapper.PersonMapper;
import com.platform.model.Person;
import com.platform.model.registration.PersonRegistration;
import com.platform.persistence.entity.PersonEntity;
import com.platform.service.ClientService;
import com.platform.service.DecoratingOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Audited
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/customers/v1/person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

  private final PersonMapper mapper;
  private final ClientService<PersonEntity> service;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/register")
  public Person register(@RequestBody PersonRegistration request) {
    return mapper.toResponse(service.save(mapper.toEntity(request)));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/{clientUsername}")
  @PreAuthorize("#clientUsername == authentication.principal")
  public Person getByUsername(@RequestParam("include") Set<DecoratingOptions> include,
                              @PathVariable("clientUsername") String clientUsername) {
    return mapper.toResponse(service.loadUserByUsernameDecorated(include, clientUsername));
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping(path = "/credentials")
  @PreAuthorize("#clientUsername == authentication.principal")
  public void updatePassword(@RequestHeader String password,
                             @RequestHeader String username) {
    service.changePassword(password, username);
  }
}

package com.platform.rest.controller;

import com.platform.aspect.audit.Audited;
import com.platform.mapper.CompanyMapper;
import com.platform.model.CompanyResponse;
import com.platform.model.registration.CompanyRegisterRequest;
import com.platform.persistence.entity.Company;
import com.platform.service.CustomerService;
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
@RequestMapping(path = "/customers/v1/company", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

  private final CompanyMapper mapper;
  private final CustomerService<Company> service;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/register")
  public CompanyResponse register(@RequestBody CompanyRegisterRequest request) {
    return mapper.toResponse(service.save(mapper.toEntity(request)));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/{username}")
  @PreAuthorize("#username == authentication.principal")
  public CompanyResponse getByUsername(@RequestParam("include") Set<DecoratingOptions> include,
                                       @PathVariable("username") String username) {
    return mapper.toResponse(service.loadUserByUsernameDecorated(include, username));
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping(path = "/credentials/{username}")
  @PreAuthorize("#username == authentication.principal")
  public void updatePassword(@RequestHeader String password,
                             @PathVariable("username") String username) {
    service.changePassword(password, username);
  }
}

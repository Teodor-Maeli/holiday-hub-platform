package com.platform.rest.controller;

import com.platform.aspect.logger.IOLogger;
import com.platform.service.decorator.DecoratingOptions;
import com.platform.persistence.entity.Company;
import com.platform.rest.mapper.CompanyRepresentativeMapper;
import com.platform.rest.resource.CompanyRegistration;
import com.platform.rest.resource.CompanyResponse;
import com.platform.service.CompanyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(
    path = CompanyController.CUSTOMERS_AUTH_V_1_COMPANY,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class CompanyController {

  public static final String CUSTOMERS_AUTH_V_1_COMPANY = "/customers-auth/v1/company";

  private final CompanyRepresentativeMapper mapper;
  private final CompanyService service;

  public CompanyController(CompanyRepresentativeMapper mapper, CompanyService service) {
    this.mapper = mapper;
    this.service = service;
  }

  @IOLogger
  @PostMapping(path = "/register")
  public ResponseEntity<CompanyResponse> register(@RequestBody CompanyRegistration request) {
    Company entity = service.save(mapper.toEntity(request));
    return ResponseEntity
        .status(CREATED)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @GetMapping(path = "/get/{clientUsername}")
  @PreAuthorize("#clientUsername == authentication.principal")
  public ResponseEntity<CompanyResponse> getByUsername(@RequestParam("include") Set<DecoratingOptions> aggregations,
                                                       @PathVariable("clientUsername") String clientUsername) {
    Company entity = service.loadUserByUsername(aggregations, clientUsername);
    return ResponseEntity
        .status(OK)
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

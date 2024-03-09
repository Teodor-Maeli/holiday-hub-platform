package com.platform.rest.controller;

import com.platform.aspect.annotation.IOLogger;
import com.platform.service.decorator.DecoratingOptions;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.mapper.CompanyRepresentativeMapper;
import com.platform.model.CompanyRegistration;
import com.platform.model.Company;
import com.platform.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
  public ResponseEntity<Company> register(@RequestBody CompanyRegistration request) {
    CompanyEntity entity = service.save(mapper.toEntity(request));
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @GetMapping(path = "/get/{clientUsername}")
//  @PreAuthorize("#clientUsername == authentication.principal")
  public ResponseEntity<Company> getByUsername(@RequestParam("include") Set<DecoratingOptions> aggregations,
                                               @PathVariable("clientUsername") String clientUsername) {
    CompanyEntity entity = service.loadUserByUsername(aggregations, clientUsername);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(mapper.toResponse(entity));
  }

  @IOLogger
  @PatchMapping(path = "/update/password")
//  @PreAuthorize("#clientUsername == authentication.principal")
  public ResponseEntity<Void> updatePassword(@RequestHeader String password,
                                             @RequestHeader String clientUsername) {
    service.changePassword(password, clientUsername);
    return ResponseEntity
        .noContent()
        .build();
  }
}

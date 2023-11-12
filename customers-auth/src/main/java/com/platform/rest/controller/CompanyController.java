package com.platform.rest.controller;

import com.platform.domain.repository.CompanyRepository;
import com.platform.service.CompanyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/customers-auth/v1/company",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class CompanyController {

  private final CompanyRepository repository;

  private final CompanyService service;

  public CompanyController(CompanyRepository repository, CompanyService service) {
    this.repository = repository;
    this.service = service;
  }
}

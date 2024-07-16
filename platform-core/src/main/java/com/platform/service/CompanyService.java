package com.platform.service;

import com.platform.mapper.CompanyMapper;
import com.platform.model.CompanyResource;
import com.platform.model.CustomerResource;
import com.platform.model.CustomerType;
import com.platform.persistence.entity.Company;
import com.platform.persistence.repository.CompanyRepository;

class CompanyService implements CustomerService {

  private final CustomerHelperService<Company, Long> helperService;
  private final CompanyMapper mapper;

  public CompanyService(CompanyRepository repository, CompanyMapper mapper) {
    this.mapper = mapper;
    this.helperService = new CustomerHelperService<>(repository);
  }

  @Override
  public CustomerResource retrieve(String username) {
    Company company = helperService.retrieve(username);
    return mapper.toResource(company);
  }

  @Override
  public CustomerResource create(CustomerResource resource) {
    Company entity = mapper.toEntityCreate((CompanyResource) resource);
    return mapper.toResource(helperService.create(entity));
  }

  @Override
  public CustomerResource update(CustomerResource resource) {
    Company entity = mapper.toEntityUpdate((CompanyResource) resource);
    return mapper.toResource(helperService.create(entity));
  }

  @Override
  public CustomerType serviceType() {
    return CustomerType.IGNORE;
  }
}

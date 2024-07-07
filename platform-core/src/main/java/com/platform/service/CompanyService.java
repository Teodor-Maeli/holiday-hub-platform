package com.platform.service;

import com.platform.persistence.entity.Company;
import com.platform.persistence.repository.CompanyRepository;

class CompanyService extends AbstractCustomerService<Company, Long, CompanyRepository> {

  public CompanyService(
      CompanyRepository companyRepository,
      Encoder encoder) {
    super(companyRepository, encoder);
  }
}

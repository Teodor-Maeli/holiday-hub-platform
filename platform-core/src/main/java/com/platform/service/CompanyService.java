package com.platform.service;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.repository.CompanyRepository;

class CompanyService extends AbstractClientService<CompanyEntity, Long, CompanyRepository> {

  public CompanyService(
      CompanyRepository companyRepository,
      Encoder encoder) {
    super(companyRepository, encoder);
  }
}

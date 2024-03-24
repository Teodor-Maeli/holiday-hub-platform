package com.platform.service;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.service.decorator.DecoratingOptions;

import java.util.Set;

public class CompanyService extends AbstractClientService<CompanyEntity, Long, CompanyRepository> {

  public CompanyService(
      CompanyRepository companyRepository,
      Encoder encoder) {
    super(companyRepository, encoder);
  }

  @Override
  public CompanyEntity loadUserByUsernameDecorated(Set<DecoratingOptions> decoratingOptions, String username) {
    return super.loadUserByUsername(username);
  }
}

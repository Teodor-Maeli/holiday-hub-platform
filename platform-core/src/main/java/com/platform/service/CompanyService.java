package com.platform.service;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.service.decorator.CompanyDecorator;
import com.platform.service.decorator.DecoratingOptions;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CompanyService extends AbstractClientService<CompanyEntity, Long, CompanyRepository> {

  private final CompanyDecorator decorator;

  protected CompanyService(
      CompanyRepository companyRepository,
      Encoder encoder,
      CompanyDecorator decorator) {
    super(companyRepository, encoder);
    this.decorator = decorator;
  }

  @Override
  public CompanyEntity loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    return decorator.loadUserByUsername(decoratingOptions, username);
  }
}

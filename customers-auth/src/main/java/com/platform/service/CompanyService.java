package com.platform.service;

import com.platform.common.model.DecoratingOptions;
import com.platform.persistence.entity.Company;
import com.platform.persistence.repository.CompanyRepository;
import com.platform.service.decorator.CompanyDecorator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CompanyService extends AbstractClientService<Company, Long, CompanyRepository> {

  private final CompanyDecorator decorator;

  protected CompanyService(
      CompanyRepository companyRepository,
      PasswordEncoder encoder,
      CompanyDecorator decorator) {
    super(companyRepository, encoder);
    this.decorator = decorator;
  }

  @Override
  public Company loadUserByUsername(Set<DecoratingOptions> decoratingOptions, String username) {
    return decorator.loadUserByUsername(decoratingOptions, username);
  }
}

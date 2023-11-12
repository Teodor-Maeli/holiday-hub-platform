package com.platform.service;

import com.platform.domain.entity.Company;
import com.platform.domain.repository.CompanyRepository;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends AbstractClientService<Company, Long, CompanyRepository> {

  protected CompanyService(
      CompanyRepository repository,
      PasswordEncoder encoder,
      SessionRegistry sessionRegistry
  ) {
    super(
        repository,
        encoder,
        sessionRegistry
    );
  }
}

package com.platform.service;

import com.platform.domain.entity.Company;
import com.platform.domain.repository.AuthenticationAuditLogRepository;
import com.platform.domain.repository.CompanyRepository;
import com.platform.domain.repository.SubscriptionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends AbstractClientService<Company, Long, CompanyRepository> {

  protected CompanyService(
      CompanyRepository repository,
      PasswordEncoder encoder,
      SubscriptionService subscriptionService,
      AuthenticationAuditLogService authenticationAuditLogService
  ) {
    super(repository, encoder, subscriptionService, authenticationAuditLogService);
  }
}

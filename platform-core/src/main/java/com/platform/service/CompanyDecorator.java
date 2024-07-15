package com.platform.service;

import com.platform.mapper.CompanyMapper;
import com.platform.model.CustomerType;
import com.platform.persistence.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyDecorator extends CustomerDecorator {

  protected CompanyDecorator(SubscriptionService subscriptionService,
                             AuthenticationAttemptService authenticationAttemptService,
                             CompanyRepository companyRepository,
                             CompanyMapper companyMapper) {
    super(subscriptionService, authenticationAttemptService, new CompanyService(companyRepository, companyMapper));
  }

  @Override
  public CustomerType serviceType() {
    return CustomerType.COMPANY;
  }
}

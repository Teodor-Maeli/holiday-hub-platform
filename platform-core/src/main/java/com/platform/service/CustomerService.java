package com.platform.service;

import com.platform.model.CustomerResource;
import com.platform.model.CustomerType;

import java.util.Set;

public interface CustomerService {

  default CustomerResource loadUserByUsernameForDecoration(Set<DecoratingOptions> decoratingOptions, String username) {
    throw new UnsupportedOperationException("Method not yet implemented!");
  }

  CustomerResource retrieve(String username);

  CustomerResource create(CustomerResource resource);

  CustomerType serviceType();
}

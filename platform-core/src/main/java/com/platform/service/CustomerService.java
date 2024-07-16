package com.platform.service;

import com.platform.model.CustomerResource;
import com.platform.model.CustomerType;

import java.util.Set;

public interface CustomerService {

  default CustomerType serviceType() {
    return CustomerType.IGNORE;
  }

  CustomerResource create(CustomerResource resource);

  CustomerResource update(CustomerResource resource);

  CustomerResource retrieve(String username);

  default CustomerResource retrieve(Set<DecoratingOptions> decoratingOptions, String username) {
    throw new UnsupportedOperationException("Method not yet implemented!");
  }
}

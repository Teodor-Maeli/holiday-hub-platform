package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.CustomerType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ServiceResolver {

  private final Map<CustomerType, CustomerService> services;

  public ServiceResolver(List<CustomerService> services) {
    this.services = services.stream()
        .collect(Collectors.toMap(CustomerService::serviceType, Function.identity()));
  }

  public CustomerService resolve(CustomerType customerType) {
    CustomerService customerService = services.get(customerType);

    if (customerService == null) {
      throw new PlatformBackendException()
          .setDetails("Could not resolve service with type::%s".formatted(customerType))
          .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return customerService;
  }

}

package com.platform.service;

import com.platform.persistence.entity.Customer;

import java.util.Set;

public interface CustomerService<E extends Customer> {

  default E loadUserByUsernameDecorated(Set<DecoratingOptions> decoratingOptions, String username) {
    throw new UnsupportedOperationException("Method not yet implemented!");
  }

  E loadUserByUsername(String username);

  E save(E entity);

  void delete(String username);

  void changePassword(String newPassword, String username);
}

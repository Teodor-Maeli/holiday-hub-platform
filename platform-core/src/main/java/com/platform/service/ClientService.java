package com.platform.service;

import com.platform.persistence.entity.ClientEntity;
import com.platform.service.decorator.DecoratingOptions;

import java.util.Set;

public interface ClientService<E extends ClientEntity> {

  E loadUserByUsernameDecorated(Set<DecoratingOptions> decoratingOptions, String username);

  E loadUserByUsername(String username);

  E save(E entity);

  void delete(String username);

  void changePassword(String newPassword, String username);
}

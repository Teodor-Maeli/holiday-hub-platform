package com.platform.service;

import com.platform.config.model.ClientUserDetails;
import com.platform.exception.PlatformBackendException;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ClientAuthService implements UserDetailsService {

  private final PersonService personService;

  private final CompanyService companyService;

  public ClientAuthService(PersonService personService, CompanyService companyService) {
    this.personService = personService;
    this.companyService = companyService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<PersonEntity> person =
        personService.loadClientByUsername(username);

    if (person.isPresent()) {
      return new ClientUserDetails(person.get());
    }

    Optional<CompanyEntity> company =
        companyService.loadClientByUsername(username);

    if (company.isPresent()) {
      return new ClientUserDetails(company.get());
    }

    throw PlatformBackendException.builder()
        .message("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
        .httpStatus(BAD_REQUEST)
        .build();
  }
}

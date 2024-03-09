package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ClientAuthService implements UserDetailsService {

  private static final String USER_DETAILS = "userDetails";

  private final PersonService personService;

  private final CompanyService companyService;

  private final HttpServletRequest request;

  public ClientAuthService(PersonService personService, CompanyService companyService, HttpServletRequest request) {
    this.personService = personService;
    this.companyService = companyService;
    this.request = request;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<PersonEntity> person =
        personService.loadClientByUsername(username);

    if (person.isPresent()) {
      return cacheAndGetAsUserDetails(person.get());
    }

    Optional<CompanyEntity> company =
        companyService.loadClientByUsername(username);

    if (company.isPresent()) {
      return cacheAndGetAsUserDetails(company.get());
    }

    throw PlatformBackendException.builder()
        .message("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
        .httpStatus(BAD_REQUEST)
        .build();
  }


  private ClientUserDetails cacheAndGetAsUserDetails(ClientEntity client) {
    ClientUserDetails userDetails = new ClientUserDetails(client);
    request.setAttribute(USER_DETAILS, userDetails);

    return userDetails;
  }
}

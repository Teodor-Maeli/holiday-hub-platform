package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.service.decorator.DecoratingOptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ClientAuthService implements UserDetailsService {

  private static final String USER_DETAILS = "userDetails";

  private static final Set<DecoratingOptions> BLOCKING_AUTH_LOGS = Collections.singleton(DecoratingOptions.BLOCKING_AUTHENTICATION_LOGS);

  private final ClientService<PersonEntity> personService;

  private final ClientService<CompanyEntity> companyService;

  private final HttpServletRequest request;

  public ClientAuthService(
      ClientService<PersonEntity> personService,
      ClientService<CompanyEntity> companyService,
      HttpServletRequest request
  ) {
    this.personService = personService;
    this.companyService = companyService;
    this.request = request;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    ClientEntity client =
        clientLoginLoader.apply(username, personService)
            .orElseGet(() -> clientLoginLoader.apply(username, companyService)
                .orElseThrow(() -> PlatformBackendException.builder()
                    .message("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
                    .httpStatus(BAD_REQUEST)
                    .build()));

    return cacheAndGetAsUserDetails(client);
  }

  private final BiFunction<String, ClientService<?>, Optional<ClientEntity>> clientLoginLoader =
      (username, service) -> Optional.of(service.loadUserByUsernameDecorated(BLOCKING_AUTH_LOGS, username));


  private ClientUserDetails cacheAndGetAsUserDetails(ClientEntity client) {
    ClientUserDetails userDetails = new ClientUserDetails(client);
    request.setAttribute(USER_DETAILS, userDetails);

    return userDetails;
  }
}

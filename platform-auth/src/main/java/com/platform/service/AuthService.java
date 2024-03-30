package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.ClientUserDetails;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.service.decorator.DecoratingOptions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
public class AuthService implements UserDetailsService {

  private static final String USER_DETAILS = "userDetails";

  private static final Set<DecoratingOptions> BLOCKING_AUTH_LOGS = Collections.singleton(DecoratingOptions.BLOCKING_AUTHENTICATION_LOGS);

  private final ClientService<PersonEntity> personService;

  private final ClientService<CompanyEntity> companyService;

  private final HttpServletRequest request;

  private final Encoder encoder;

  public AuthService(
      ClientService<PersonEntity> personService,
      ClientService<CompanyEntity> companyService,
      HttpServletRequest request,
      Encoder encoder
  ) {
    this.personService = personService;
    this.companyService = companyService;
    this.request = request;
    this.encoder = encoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    ClientEntity client = loadUserByUsernameInternal(username);

    return cacheAndGetAsUserDetails(client);
  }

  public void startAccountUnlocking(String username) {
    //TODO
    //Sent email using SMTP
  }

  public void completeAccountUnlocking(String username, String unlockingCode) {
    ClientEntity client = loadUserByUsernameInternal(username);

    if (shouldUnlock(client, unlockingCode)) {
      unlockAccount(client);
    } else {
      throw PlatformBackendException.builder()
          .details("Either unlocking code doesn't match or client is not eligible for self unlocking."
              + "Please contact customer support for assistance.")
          .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
          .build();
    }
  }

  private void unlockAccount(ClientEntity client) {
    client.getAuthenticationLogs().forEach(log -> log.setStatusResolved(Boolean.TRUE));
    if (client instanceof PersonEntity person) {
      personService.save(person);
    } else if (client instanceof CompanyEntity company) {
      companyService.save(company);
    } else {
      throw PlatformBackendException.builder()
          .details("Unable to unlock client account, please contact customer support for assistance!")
          .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  private boolean shouldUnlock(ClientEntity client, String unlockingCode) {
    return ! Boolean.TRUE.equals(client.getAccountLocked())
        && client.getAuthenticationLogs().stream()
        .anyMatch(log -> encoder.matches(unlockingCode, log.getHashedUnlockingCode()));
  }

  private ClientEntity loadUserByUsernameInternal(String username) {
    return clientLoginLoader.apply(username, personService)
        .orElseGet(() -> clientLoginLoader.apply(username, companyService)
            .orElseThrow(() -> PlatformBackendException.builder()
                .message("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
                .httpStatus(BAD_REQUEST)
                .build()));
  }

  private final BiFunction<String, ClientService<?>, Optional<ClientEntity>> clientLoginLoader =
      (username, service) -> Optional.of(service.loadUserByUsernameDecorated(BLOCKING_AUTH_LOGS, username));


  private ClientUserDetails cacheAndGetAsUserDetails(ClientEntity client) {
    ClientUserDetails userDetails = new ClientUserDetails(client);
    request.setAttribute(USER_DETAILS, userDetails);

    return userDetails;
  }
}

package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.model.EmailMessageDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.util.email.EmailSender;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AuthService implements UserDetailsService {

  private static final String EMAIL_SUBJECT = "Account unlocking for:%s";
  private static final String USER_DETAILS = "userDetails";
  private static final Set<DecoratingOptions> BLOCKING_AUTH_LOGS = Collections.singleton(DecoratingOptions.BLOCKING_AUTHENTICATION_LOGS);

  @Value("${platform.security.accounts.auto-unlocking.addresses.complete}")
  private String completionAddress;
  @Value("${platform.security.accounts.auto-unlocking.addresses.reply}")
  private String replyAddress;
  private final ClientService<PersonEntity> personService;
  private final ClientService<CompanyEntity> companyService;
  private final HttpServletRequest request;
  private final EmailSender emailSender;
  private final Encoder encoder;
  private final BiFunction<String, ClientService<?>, Optional<ClientEntity>> loadWithBlockingLogsFunc =
      (username, service) -> Optional.of(service.loadUserByUsernameDecorated(BLOCKING_AUTH_LOGS, username));

  public AuthService(
      ClientService<PersonEntity> personService,
      ClientService<CompanyEntity> companyService,
      HttpServletRequest request,
      EmailSender emailSender,
      Encoder encoder
  ) {
    this.personService = personService;
    this.companyService = companyService;
    this.request = request;
    this.emailSender = emailSender;
    this.encoder = encoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    ClientEntity client = loadUserByUsernameInternal(username);

    return cacheAndGetAsUserDetails(client);
  }

  /**
   * Initiates account unlocking.
   * Performs checks and determines if account is eligible for unlocking,
   * only then issues account unlocking code, else exception is thrown.
   * For more information please see {@link AuthenticationStatusReason}.
   *
   * @param username client to be loaded.
   */
  public void startAccountUnlocking(String username) {
    ClientEntity client = loadUserByUsernameInternal(username);

    if (isAccountLocked(client)) {
      issueAccountUnlockingCode(client);
    } else {
      throw PlatformBackendException.builder()
          .details("Account does not need to be unlocked!")
          .httpStatus(BAD_REQUEST)
          .build();
    }
  }

  private void issueAccountUnlockingCode(ClientEntity client) {
    String rawUnlockingCode = UUID.randomUUID().toString();

    EmailMessageDetails emailMessageDetails =
        EmailMessageDetails.create()
            .withText(buildEmailTextBody(rawUnlockingCode, client.getUsername()))
            .withTo(client.getEmailAddress())
            .withSentDate(new Date())
            .withFrom(replyAddress)
            .withSubject(EMAIL_SUBJECT.formatted(client.getUsername()));

    emailSender.sent(emailMessageDetails);

    String hashedUnlockingCode = encoder.encode(rawUnlockingCode);
    client.getAuthenticationLogs().forEach(log -> log.setEncodedUnlockingCode(hashedUnlockingCode));
    saveClient(client);
  }

  /**
   * Completes account unlocking.
   * Performs checks and determines if loaded entity is eligible for unlocking,
   * only then resolves all blocking logs else exception is thrown.
   * For more information please see {@link AuthenticationStatusReason}.
   *
   * @param username      client to be loaded.
   * @param unlockingCode unlocking code that should match against any of the authentication logs.
   */
  public void completeAccountUnlocking(String username, String unlockingCode) {
    ClientEntity client = loadUserByUsernameInternal(username);

    if (shouldUnlock(client, unlockingCode)) {
      unlockAccount(client, null);
    } else {
      throw PlatformBackendException.builder()
          .details("Either unlocking code doesn't match or client is not eligible for self unlocking."
              + "Please contact customer support for assistance.")
          .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
          .build();
    }
  }

  private void unlockAccount(ClientEntity client, String updatedBy) {
    for (AuthenticationLogEntity log : client.getAuthenticationLogs()) {

      log.setStatusResolved(Boolean.TRUE);
      log.setUpdatedBy(Objects.isNull(updatedBy)
                       ? client.getUsername()
                       : updatedBy);
    }
    saveClient(client);
  }

  private void saveClient(ClientEntity client) {
    if (client instanceof PersonEntity person) {
      personService.save(person);
    } else if (client instanceof CompanyEntity company) {
      companyService.save(company);
    } else {
      throw PlatformBackendException.builder()
          .details("Saving could not be completed, please contact customer support for assistance!")
          .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  private boolean shouldUnlock(ClientEntity client, String unlockingCode) {
    return isAccountLocked(client)
        && client.getAuthenticationLogs().stream()
        .anyMatch(log -> encoder.matches(unlockingCode, log.getEncodedUnlockingCode()));
  }

  private boolean isAccountLocked(ClientEntity client) {
    ClientUserDetails clientUserDetails = new ClientUserDetails(client);
    return Boolean.TRUE.equals(client.getAccountLocked())
        || !clientUserDetails.isAccountNonLocked()
        || !clientUserDetails.isEnabled();
  }

  private ClientEntity loadUserByUsernameInternal(String username) {
    return loadWithBlockingLogsFunc.apply(username, personService)
        .orElseGet(() -> loadWithBlockingLogsFunc.apply(username, companyService)
            .orElseThrow(() -> PlatformBackendException.builder()
                .message("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
                .httpStatus(BAD_REQUEST)
                .build()));
  }

  private ClientUserDetails cacheAndGetAsUserDetails(ClientEntity client) {
    ClientUserDetails userDetails = new ClientUserDetails(client);
    request.setAttribute(USER_DETAILS, userDetails);

    return userDetails;
  }

  private String buildEmailTextBody(String unlockingCode, String userName) {
    return "Dear %s, ".formatted(userName) +
        "Please find below your account unlocking link." +
        "\n" +
        "Click to unlock :%s%s".formatted(completionAddress, unlockingCode);
  }
}

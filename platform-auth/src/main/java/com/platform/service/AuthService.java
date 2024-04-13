package com.platform.service;

import com.platform.exception.PlatformBackendException;
import com.platform.model.AccountUnlock;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.ClientUserDetails;
import com.platform.model.EmailMessageDetails;
import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import com.platform.persistence.entity.CompanyEntity;
import com.platform.persistence.entity.PersonEntity;
import com.platform.util.SecurityUtils;
import com.platform.util.email.EmailSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AuthService implements UserDetailsService {

  private static final Set<DecoratingOptions> BLOCKING_AUTH_LOGS = Collections.singleton(DecoratingOptions.BLOCKING_AUTHENTICATION_LOGS);
  private static final String EMAIL_SUBJECT = "Account unlocking for:%s";
  private static final String USER_DETAILS = "userDetails";
  private static final String USERNAME_PARAM = "username";
  private static final String UNLOCKING_CODE_PARAM = "unlockingCode";

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
      Encoder encoder) {
    this.personService = personService;
    this.companyService = companyService;
    this.request = request;
    this.emailSender = emailSender;
    this.encoder = encoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    ClientEntity client = loadUserByUsernameInternal(username);

    return cacheIntoRequestAsClientUserDetails(client);
  }

  /**
   * Initiates account unlocking.
   * Performs checks and determines if account is eligible for unlocking,
   * only then issues account unlocking code, else exception is thrown.
   * For more information please see {@link AuthenticationStatusReason}.
   *
   * @param username client to be loaded.
   */
  @Transactional(rollbackOn = Exception.class)
  public AccountUnlock startAccountUnlocking(String username) {
    ClientEntity client = loadUserByUsernameInternal(username);

    if (!isAccountLocked(client)) {
      throw PlatformBackendException.builder()
          .details("Account does not need to be unlocked!")
          .httpStatus(BAD_REQUEST)
          .build();
    }

    return issueAccountUnlockingCode(client);
  }

  private AccountUnlock issueAccountUnlockingCode(ClientEntity client) {
    String rawUnlockingCode = UUID.randomUUID().toString();

    String partnerUnlockingUrl = UriComponentsBuilder.fromHttpUrl(client.getRedirectUrl())
        .queryParam(USERNAME_PARAM, client.getUsername())
        .queryParam(UNLOCKING_CODE_PARAM, rawUnlockingCode)
        .toUriString();

    EmailMessageDetails emailMessageDetails =
        EmailMessageDetails.create()
            .withSubject(EMAIL_SUBJECT.formatted(client.getUsername()))
            .withTo(client.getEmailAddress())
            .withFrom(replyAddress)
            .withReplyTo(replyAddress)
            .withText(buildEmailTextBody(partnerUnlockingUrl, client.getUsername()))
            .withSentDate(new Date());

    emailSender.send(emailMessageDetails);

    updateAuthLogsWithCode(client, encoder.encode(rawUnlockingCode));
    saveClient(client);

    return new AccountUnlock(
        AccountUnlock.State.INITIATED,
        client.getRedirectUrl(),
        client.getReturnUrl(),
        replyAddress
    );
  }

  private void updateAuthLogsWithCode(ClientEntity client, String hashedUnlockingCode) {
    for (AuthenticationLogEntity log : client.getAuthenticationLogs()) {
      log.setEncodedUnlockingCode(hashedUnlockingCode);
    }
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
  @Transactional(rollbackOn = Exception.class)
  public AccountUnlock completeAccountUnlocking(String username, String unlockingCode) {
    ClientEntity client = loadUserByUsernameInternal(username);

    if (!shouldUnlock(client, unlockingCode)) {
      throw PlatformBackendException.builder()
          .details("Either unlocking code doesn't match or client is not eligible for unlocking."
              + "Please contact customer support for assistance.")
          .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
          .build();
    }

    return unlockAccount(client);
  }

  private AccountUnlock unlockAccount(ClientEntity client) {
    String updatedBy =
        SecurityUtils.getPrincipal()
            .map(ClientUserDetails::getUsername)
            .orElse(client.getUsername());

    resolveLogsForClient(client, updatedBy);

    client.setAccountLocked(Boolean.FALSE);
    saveClient(client);

    return new AccountUnlock(
        AccountUnlock.State.COMPLETED,
        client.getRedirectUrl(),
        client.getReturnUrl(),
        replyAddress
    );
  }

  private void resolveLogsForClient(ClientEntity client, String updatedBy) {
    for (AuthenticationLogEntity log : client.getAuthenticationLogs()) {
      log.setStatusResolved(Boolean.TRUE);
      log.setUpdatedBy(updatedBy);
    }
  }

  private void saveClient(ClientEntity client) {
    if (client instanceof PersonEntity person) {
      personService.save(person);
    } else if (client instanceof CompanyEntity company) {
      companyService.save(company);
    } else {
      throw PlatformBackendException.builder()
          .details("Saving could not be completed, please contact technical support for assistance!")
          .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  private boolean shouldUnlock(ClientEntity client, String unlockingCode) {
    return canUnlockByAdmin() || canUnlockByClient(client, unlockingCode);
  }

  private boolean canUnlockByAdmin() {
    return SecurityUtils.getPrincipal().map(ClientUserDetails::isAdmin).orElse(false);
  }

  private boolean canUnlockByClient(ClientEntity client, String unlockingCode) {
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

  private ClientUserDetails cacheIntoRequestAsClientUserDetails(ClientEntity client) {
    ClientUserDetails userDetails = new ClientUserDetails(client);
    request.setAttribute(USER_DETAILS, userDetails);

    return userDetails;
  }

  private String buildEmailTextBody(String followUrl, String userName) {
    URL resource = AuthService.class.getClassLoader().getResource("service/unlock-initiated.html");
    String text;

    try (InputStream is = resource.openStream()) {
      text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      text = text.replace("{{username}}", userName);
      text = text.replace("{{followUrl}}", followUrl);
      text = text.replace("{{replyAddress}}", replyAddress);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return text;
  }
}

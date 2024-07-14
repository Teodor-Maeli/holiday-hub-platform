package com.platform.service;

import com.platform.config.PlatformSecurityProperties;
import com.platform.exception.PlatformBackendException;
import com.platform.model.AccountUnlock;
import com.platform.model.AuthenticationStatusReason;
import com.platform.model.CustomerUserDetails;
import com.platform.model.EmailMessageDetails;
import com.platform.persistence.entity.AuthenticationAttempt;
import com.platform.persistence.entity.Company;
import com.platform.persistence.entity.Configuration;
import com.platform.persistence.entity.Customer;
import com.platform.persistence.entity.Person;
import com.platform.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;
import java.util.function.BiFunction;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

  private static final String EMAIL_SUBJECT = "Account unlocking for:%s";
  private static final String USER_DETAILS = "userDetails";
  private static final String USERNAME_PARAM = "username";
  private static final String UNLOCKING_CODE_PARAM = "unlockingCode";


  private final PlatformSecurityProperties properties;
  private final CustomerService<Person> personService;
  private final CustomerService<Company> companyService;
  private final HttpServletRequest request;
  private final EmailService emailService;
  private final Encoder encoder;
  private final BiFunction<String, CustomerService<?>, Optional<Customer>> getBlockingAuthFailures =
      (username, service) -> Optional.of(
          service.loadUserByUsernameForDecoration(
              Collections.singleton(DecoratingOptions.BLOCKING_AUTHENTICATION_ATTEMPTS), username));

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Customer client = loadUserByUsernameInternal(username);

    return cacheIntoRequestAsClientUserDetails(client);
  }

  /**
   * Initiates account unlocking.
   * Performs checks and determines if account is eligible for unlocking,
   * only then issues account unlocking code, else exception is thrown.
   *
   * @param username username of customer to be unlocked.
   */
  @Transactional(rollbackOn = Exception.class)
  public AccountUnlock startAccountUnlocking(String username) {
    Customer client = loadUserByUsernameInternal(username);

    if (!isAccountLocked(client)) {
      throw new PlatformBackendException().setDetails("Account does not need to be unlocked!").setHttpStatus(BAD_REQUEST);
    }

    return issueAccountUnlockingCode(client, null);
  }

  private AccountUnlock issueAccountUnlockingCode(Customer client, Configuration configuration) {
    String rawUnlockingCode = UUID.randomUUID().toString();

    String partnerUnlockingUrl = UriComponentsBuilder.fromHttpUrl(configuration.getRedirectUrl())
        .queryParam(USERNAME_PARAM, client.getUsername())
        .queryParam(UNLOCKING_CODE_PARAM, rawUnlockingCode)
        .toUriString();

    EmailMessageDetails emailMessageDetails =
        EmailMessageDetails.of()
            .setSubject(EMAIL_SUBJECT.formatted(client.getUsername()))
            .setTo(new String[]{client.getEmailAddress()})
            .setFrom(properties.getUnlockReplyEmail())
            .setReplyTo(properties.getUnlockReplyEmail())
            .setText(buildEmailTextBody(partnerUnlockingUrl, client.getUsername()))
            .setSentDate(new Date());

    emailService.send(emailMessageDetails);

    updateAttemptWithCode(client, encoder.encode(rawUnlockingCode));
    saveCustomer(client);

    return new AccountUnlock(AccountUnlock.State.INITIATED,
        configuration.getRedirectUrl(),
        configuration.getReturnUrl(),
        properties.getUnlockReplyEmail()
    );
  }

  private void updateAttemptWithCode(Customer client, String hashedUnlockingCode) {
    for (AuthenticationAttempt log : client.getAuthenticationAttempts()) {
      log.setEncodedUnlockingCode(hashedUnlockingCode);
    }
  }

  /**
   * Completes account unlocking.
   * Performs checks and determines if loaded entity is eligible for unlocking,
   * only then resolves all blocking auth attempts else exception is thrown.
   * For more information please see {@link AuthenticationStatusReason}.
   *
   * @param username      client to be loaded.
   * @param unlockingCode unlocking code that should match against any of the authentication attempts.
   */
  @Transactional(rollbackOn = Exception.class)
  public AccountUnlock completeAccountUnlocking(String username, String unlockingCode) {
    Customer client = loadUserByUsernameInternal(username);

    if (!shouldUnlock(client, unlockingCode)) {
      throw new PlatformBackendException()
          .setDetails("""
              Either unlocking code doesn't match or client is not eligible for unlocking.
              Please contact customer support for assistance.""")
          .setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }

    return unlockAccount(client, null);
  }

  private AccountUnlock unlockAccount(Customer client, Configuration configuration) {
    String updatedBy =
        SecurityUtils.getPrincipal()
            .map(CustomerUserDetails::getUsername)
            .orElse(client.getUsername());

    resolveBlockingAttempts(client, updatedBy);

    client.setAccountLocked(Boolean.FALSE);
    saveCustomer(client);

    return new AccountUnlock(AccountUnlock.State.COMPLETED, configuration.getRedirectUrl(), configuration.getReturnUrl(), properties.getUnlockReplyEmail());
  }

  private void resolveBlockingAttempts(Customer client, String updatedBy) {
    for (AuthenticationAttempt log : client.getAuthenticationAttempts()) {
      log.setStatusResolved(Boolean.TRUE);
      log.setUpdatedBy(updatedBy);
    }
  }

  private void saveCustomer(Customer customer) {
    if (customer instanceof Person person) {
      personService.save(person);
    } else if (customer instanceof Company company) {
      companyService.save(company);
    } else {
      throw new PlatformBackendException().setDetails("Saving could not be completed, please contact technical support for assistance!")
          .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean shouldUnlock(Customer client, String unlockingCode) {
    return canUnlockByAdmin() || canUnlockByClient(client, unlockingCode);
  }

  private boolean canUnlockByAdmin() {
    return SecurityUtils.getPrincipal().map(CustomerUserDetails::isAdmin).orElse(false);
  }

  private boolean canUnlockByClient(Customer client, String unlockingCode) {
    return isAccountLocked(client)
        && client.getAuthenticationAttempts().stream()
        .anyMatch(log -> encoder.matches(unlockingCode, log.getEncodedUnlockingCode()));
  }

  private boolean isAccountLocked(Customer client) {
    CustomerUserDetails customerUserDetails = new CustomerUserDetails(client);

    return Boolean.TRUE.equals(client.getAccountLocked())
        || !customerUserDetails.isAccountNonLocked()
        || !customerUserDetails.isEnabled();
  }

  private Customer loadUserByUsernameInternal(String username) {
    return getBlockingAuthFailures.apply(username, personService)
        .orElseGet(() -> getBlockingAuthFailures.apply(username, companyService)
            .orElseThrow(() -> new PlatformBackendException()
                .setMessage("Failed to LOAD user, USERNAME: %s non-existent or suspended!".formatted(username))
                .setHttpStatus(BAD_REQUEST)));
  }

  private CustomerUserDetails cacheIntoRequestAsClientUserDetails(Customer client) {
    CustomerUserDetails userDetails = new CustomerUserDetails(client);

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
      text = text.replace("{{replyAddress}}", properties.getUnlockReplyEmail());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return text;
  }
}

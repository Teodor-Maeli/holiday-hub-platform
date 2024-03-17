package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.platform.model.ConsumerAuthority;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Base entity class.
 * Since 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class ClientEntity {

  @Id
  @Column(name = "ID", updatable = false, unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "USERNAME", updatable = false, unique = true, nullable = false)
  private String username;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @Column(name = "EMAIL_ADDRESS", unique = true, nullable = false)
  private String emailAddress;

  @Column(name = "REGISTERED_DATE", updatable = false, nullable = false)
  @CreatedDate
  private LocalDateTime registeredDate;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<AuthenticationLogEntity> authenticationLogs;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<SubscriptionEntity> subscriptions;

  @Column(name = "CONSUMER_AUTHORITIES")
  @ElementCollection(targetClass = ConsumerAuthority.class, fetch = FetchType.LAZY)
  private Set<ConsumerAuthority> consumerAuthorities;

  @Column(name = "ACCOUNT_LOCKED")
  private Boolean accountLocked;


  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getRegisteredDate() {
    return registeredDate;
  }

  public void setRegisteredDate(LocalDateTime registeredDate) {
    this.registeredDate = registeredDate;
  }

  public Set<ConsumerAuthority> getConsumerAuthorities() {
    if (Hibernate.isInitialized(consumerAuthorities)) {
      return consumerAuthorities;
    }

    return Collections.emptySet();
  }

  public void setConsumerAuthorities(Set<ConsumerAuthority> consumerAuthorities) {
    this.consumerAuthorities = consumerAuthorities;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public List<AuthenticationLogEntity> getAuthenticationLogs() {
    if (Hibernate.isInitialized(authenticationLogs)) {
      return authenticationLogs;
    }

    return Collections.emptyList();
  }

  public void setAuthenticationLogs(List<AuthenticationLogEntity> authenticationLogs) {
    this.authenticationLogs = authenticationLogs;
  }

  public List<SubscriptionEntity> getSubscriptions() {
    if (Hibernate.isInitialized(subscriptions)) {
      return subscriptions;
    }

    return Collections.emptyList();
  }

  public void setSubscriptions(List<SubscriptionEntity> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public Boolean getAccountLocked() {
    return accountLocked;
  }

  public void setAccountLocked(Boolean accountLocked) {
    this.accountLocked = accountLocked;
  }
}

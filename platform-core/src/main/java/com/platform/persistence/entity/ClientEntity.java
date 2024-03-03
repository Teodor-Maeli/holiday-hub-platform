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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Base entity class.
 * Since 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
  private List<AuthenticationLogEntity> authenticationAuditLogs;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<SubscriptionEntity> subscriptions;

  @Column(name = "CONSUMER_AUTHORITIES")
  @ElementCollection(targetClass = ConsumerAuthority.class, fetch = FetchType.LAZY)
  private Set<ConsumerAuthority> consumerAuthorities;

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

  public void setConsumerAuthorities(Set<ConsumerAuthority> consumerAuthorities) {
    this.consumerAuthorities = consumerAuthorities;
  }

  public Set<ConsumerAuthority> getConsumerAuthorities() {
    return consumerAuthorities;
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

  public List<AuthenticationLogEntity> getAuthenticationTokenEntities() {
    return authenticationAuditLogs;
  }

  public void setAuthenticationTokenEntities(List<AuthenticationLogEntity> authTokensEntities) {
    this.authenticationAuditLogs = authTokensEntities;
  }

  public List<AuthenticationLogEntity> getAuthenticationAuditLogs() {
    return authenticationAuditLogs;
  }

  public void setAuthenticationAuditLogs(List<AuthenticationLogEntity> authenticationAuditLogs) {
    this.authenticationAuditLogs = authenticationAuditLogs;
  }

  public List<SubscriptionEntity> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(List<SubscriptionEntity> subscriptions) {
    this.subscriptions = subscriptions;
  }
}

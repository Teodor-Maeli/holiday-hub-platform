package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.platform.model.ConsumerAuthority;
import jakarta.persistence.CascadeType;
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
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class CustomerEntity {

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

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<AuthenticationLogEntity> authenticationLogs;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<SubscriptionEntity> subscriptions;

  @Column(name = "CONSUMER_AUTHORITIES")
  @ElementCollection(targetClass = ConsumerAuthority.class, fetch = FetchType.EAGER)
  private Set<ConsumerAuthority> consumerAuthorities;

  @Column(name = "ACCOUNT_LOCKED")
  private Boolean accountLocked;

  public Set<ConsumerAuthority> getConsumerAuthorities() {
    if (Hibernate.isInitialized(consumerAuthorities)) {
      return consumerAuthorities;
    }

    return Collections.emptySet();
  }

  public List<AuthenticationLogEntity> getAuthenticationLogs() {
    if (Hibernate.isInitialized(authenticationLogs)) {
      return authenticationLogs;
    }

    return Collections.emptyList();
  }
  public List<SubscriptionEntity> getSubscriptions() {
    if (Hibernate.isInitialized(subscriptions)) {
      return subscriptions;
    }

    return Collections.emptyList();
  }

}

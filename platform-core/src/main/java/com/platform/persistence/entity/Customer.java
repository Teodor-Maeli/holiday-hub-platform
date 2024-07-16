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
import jakarta.persistence.OneToOne;
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
public abstract class Customer {

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

  @Column(name = "CREATED_ON", updatable = false, nullable = false)
  @CreatedDate
  private LocalDateTime createdOn;

  @Column(name = "UPDATED_ON", updatable = false, nullable = false)
  @CreatedDate
  private LocalDateTime updatedOn;

  @Column(name = "AUTHORITIES")
  @ElementCollection(targetClass = ConsumerAuthority.class, fetch = FetchType.EAGER)
  private Set<ConsumerAuthority> authorities;

  @Column(name = "LOCKED")
  private Boolean locked;

  @OneToOne(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
  @JsonManagedReference
  private Configuration configuration;

  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<AuthenticationAttempt> authenticationAttempts;

  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Subscription> subscriptions;

  public Set<ConsumerAuthority> getAuthorities() {
    if (Hibernate.isInitialized(authorities)) {
      return authorities;
    }

    return Collections.emptySet();
  }

  public List<AuthenticationAttempt> getAuthenticationAttempts() {
    if (Hibernate.isInitialized(authenticationAttempts)) {
      return authenticationAttempts;
    }

    return Collections.emptyList();
  }

  public List<Subscription> getSubscriptions() {
    if (Hibernate.isInitialized(subscriptions)) {
      return subscriptions;
    }

    return Collections.emptyList();
  }

}

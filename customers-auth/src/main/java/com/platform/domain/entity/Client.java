package com.platform.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.platform.common.model.AuthenticationStatus;
import com.platform.common.model.Role;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base entity class.
 * Since 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class)
public abstract class Client implements UserDetails {

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
  private List<AuthenticationAuditLog> authenticationAuditLogs;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Subscription> subscriptions;

  @Column(name = "ROLES")
  @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
  private Set<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean isEnabled() {
    if (roles == null || roles.isEmpty()) {
      return true;
    }

   return authenticationAuditLogs.stream().noneMatch(this::isLocked);
  }

  private boolean isLocked(AuthenticationAuditLog authInfo) {
    return (authInfo.getAuthenticationStatus() == AuthenticationStatus.LOCKED
        || authInfo.getAuthenticationStatus() == AuthenticationStatus.BLACKLISTED)
        && Boolean.FALSE.equals(authInfo.getStatusResolved());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
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

  public List<AuthenticationAuditLog> getAuthenticationTokenEntities() {
    return authenticationAuditLogs;
  }

  public void setAuthenticationTokenEntities(List<AuthenticationAuditLog> authTokensEntities) {
    this.authenticationAuditLogs = authTokensEntities;
  }

  public List<AuthenticationAuditLog> getAuthenticationAuditLogs() {
    return authenticationAuditLogs;
  }

  public void setAuthenticationAuditLogs(List<AuthenticationAuditLog> authenticationAuditLogs) {
    this.authenticationAuditLogs = authenticationAuditLogs;
  }

  public List<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(List<Subscription> subscriptions) {
    this.subscriptions = subscriptions;
  }
}

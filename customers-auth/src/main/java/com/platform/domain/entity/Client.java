package com.platform.domain.entity;

import com.platform.models.Role;
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
 * 27.05.2023.
 *
 * <p>Base entity class.</p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
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

  @Column(name = "ENABLED", nullable = false)
  private Boolean enabled;

  @Column(name = "REGISTERED_DATE", updatable = false, nullable = false)
  @CreatedDate
  private LocalDateTime registeredDate;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  private List<Session> sessions;

  @PrimaryKeyJoinColumn(name = "SUBSCRIPTION")
  @OneToOne(mappedBy = "client", fetch = FetchType.LAZY)
  private Subscription subscription;


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
    return Boolean.TRUE.equals(getEnabled());
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

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
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

  public List<Session> getSessions() {
    return sessions;
  }

  public void setSessions(List<Session> sessions) {
    this.sessions = sessions;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }
}

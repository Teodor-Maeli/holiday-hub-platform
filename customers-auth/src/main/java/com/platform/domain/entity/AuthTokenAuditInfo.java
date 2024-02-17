package com.platform.domain.entity;

import com.platform.common.model.AuthenticationStatus;
import com.platform.common.model.AuthenticationStatusReason;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUTH_TOKEN_AUDIT_INFO")
@EntityListeners(AuditingEntityListener.class)
public class AuthTokenAuditInfo {

  @Id
  @Column(name = "ID", updatable = false, unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JoinColumn(name = "CLIENT_ID", nullable = false)
  private Client client;

  @CreatedDate
  @Column(name = "CREATED_DATE")
  private LocalDateTime createdDate;

  @Column(name = "BLACK_LISTED_DATE")
  private LocalDateTime blackListedDate;

  @Column(name = "AUTHENTICATION_STATUS")
  @Enumerated(EnumType.STRING)
  private AuthenticationStatus authenticationStatus;

  @Column(name = "STATUS_REASON", nullable = false)
  @Enumerated(EnumType.STRING)
  private AuthenticationStatusReason statusReason;

  @Column(name = "STATUS_RESOLVED")
  private Boolean statusResolved;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getBlackListedDate() {
    return blackListedDate;
  }

  public void setBlackListedDate(LocalDateTime blackListedDate) {
    this.blackListedDate = blackListedDate;
  }

  public AuthenticationStatus getAuthenticationStatus() {
    return authenticationStatus;
  }

  public void setAuthenticationStatus(AuthenticationStatus authenticationStatus) {
    this.authenticationStatus = authenticationStatus;
  }

  public AuthenticationStatusReason getStatusReason() {
    return statusReason;
  }

  public void setStatusReason(AuthenticationStatusReason statusReason) {
    this.statusReason = statusReason;
  }

  public Boolean getStatusResolved() {
    return statusResolved;
  }

  public void setStatusResolved(Boolean statusResolved) {
    this.statusResolved = statusResolved;
  }
}

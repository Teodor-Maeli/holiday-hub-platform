package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUTHENTICATION_LOG")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationLogEntity {

  @Id
  @Column(name = "ID", updatable = false, unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "username", name = "CLIENT_ID", nullable = false)
  @JsonBackReference
  private ClientEntity client;

  @CreatedDate
  @Column(name = "CREATED_DATE", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "UPDATED_DATE")
  private LocalDateTime updatedDate;

  @Column(name = "AUTHENTICATION_STATUS", nullable = false)
  @Enumerated(EnumType.STRING)
  private AuthenticationStatus authenticationStatus;

  @Column(name = "STATUS_REASON", nullable = false)
  @Enumerated(EnumType.STRING)
  private AuthenticationStatusReason statusReason;

  @Column(name = "MESSAGE")
  private String message;

  @Column(name = "STATUS_RESOLVED")
  private Boolean statusResolved;

  @Column(name = "UPDATED_BY")
  private String updatedBy;

  @Column(name = "ENCODED_UNLOCKING_CODE")
  private String encodedUnlockingCode;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ClientEntity getClient() {
    return client;
  }

  public void setClient(ClientEntity client) {
    this.client = client;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getEncodedUnlockingCode() {
    return encodedUnlockingCode;
  }

  public void setEncodedUnlockingCode(String hashedUnlockingCode) {
    this.encodedUnlockingCode = hashedUnlockingCode;
  }
}

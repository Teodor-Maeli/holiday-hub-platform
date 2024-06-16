package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.platform.model.AuthenticationStatus;
import com.platform.model.AuthenticationStatusReason;
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
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
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
}

package com.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.platform.model.SubscriptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "SUBSCRIPTION")
public class SubscriptionEntity {

  @Id
  @Column(name = "ID", updatable = false, unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CLIENT_ID", nullable = false)
  @JsonBackReference
  private ClientEntity client;

  @Column(name = "START_DATE")
  private LocalDateTime startDate;

  @Column(name = "END_DATE")
  private LocalDateTime endDate;

  @Column(name = "ACTIVE")
  private Boolean active;

  @Column(name = "SUBSCRIPTION_TYPE")
  private SubscriptionType subscriptionType;
}

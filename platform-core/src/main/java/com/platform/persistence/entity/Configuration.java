package com.platform.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CONFIGURATION")
public class Configuration {

  @Id
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @MapsId
  private Customer customer;

  @Column(name = "REDIRECT_URL")
  private String redirectUrl;

  @Column(name = "RETURN_URL")
  private String returnUrl;
}

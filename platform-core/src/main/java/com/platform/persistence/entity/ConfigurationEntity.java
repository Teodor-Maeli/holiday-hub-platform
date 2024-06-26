package com.platform.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//TODO create configuration.
public class ConfigurationEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private CompanyEntity company;

  @Column(name = "REDIRECT_URL")
  private String redirectUrl;

  @Column(name = "RETURN_URL")
  private String returnUrl;
}

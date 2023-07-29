package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LegalEntity extends PlatformClient {

    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "COMPANY_NUMBER")
    private String companyNumber;
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;
}

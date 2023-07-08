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

    @Column
    private String companyName;
    @Column
    private String companyNumber;
    @Column
    private String contactPerson;
}

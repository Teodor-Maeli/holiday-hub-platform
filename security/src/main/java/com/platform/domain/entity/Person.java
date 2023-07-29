package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Person extends PlatformClient {

    @Column(name = "GIVEN_NAME")
    private String givenName;
    @Column(name = "FAMILY_NAME")
    private String familyName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;
}

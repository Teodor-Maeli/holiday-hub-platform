package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Person extends PlatformClient {

    @Column
    private String givenName;
    @Column
    private String familyName;
    @Column
    private String middleName;
    @Column
    private LocalDate birthDate;
}

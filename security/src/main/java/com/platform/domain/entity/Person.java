package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
public class Person extends PlatformClient {

    @Column
    private String fullName;
    @Column
    private LocalDate birthDate;
    @Column
    private LocalDateTime subscriptionStarts;
    @Column
    private LocalDateTime subscriptionEnds;
    @Column
    private boolean isPremiumEnabled;
}

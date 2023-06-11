package com.platform.authorizationserver.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity
public class LegalEntity extends Client {

    @Column
    private String companyName;
    @Column
    private String companyNumber;
    @Column
    private LocalDateTime subscriptionStarts;
    @Column
    private LocalDateTime subscriptionEnds;
    @Column
    private boolean isPremiumEnabled;
}

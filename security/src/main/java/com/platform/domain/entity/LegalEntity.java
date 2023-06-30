package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
public class LegalEntity extends PlatformClient {

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

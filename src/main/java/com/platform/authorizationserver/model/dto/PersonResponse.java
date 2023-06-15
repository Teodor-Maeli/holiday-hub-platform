package com.platform.authorizationserver.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PersonResponse extends PlatformClientResponse {

    private String fullName;
    private LocalDate birthDate;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private boolean isPremiumEnabled;
}

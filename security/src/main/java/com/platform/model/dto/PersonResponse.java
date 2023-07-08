package com.platform.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonResponse extends PlatformClientResponse {

    private String fullName;
    private LocalDate birthDate;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private boolean isPremiumEnabled;
}

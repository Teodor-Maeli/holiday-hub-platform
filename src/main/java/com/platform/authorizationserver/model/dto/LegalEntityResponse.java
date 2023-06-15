package com.platform.authorizationserver.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LegalEntityResponse extends PlatformClientResponse {

    private String companyName;
    private String companyNumber;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private boolean isPremiumEnabled;
}

package com.platform.model.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LegalEntityResponse extends PlatformClientResponse {

    private String companyName;
    private String companyNumber;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private Boolean isPremiumEnabled;
}

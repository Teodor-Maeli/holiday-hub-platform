package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class LegalEntityResponse extends PlatformClientResponse {

    private String companyName;
    private String companyNumber;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private boolean isPremiumEnabled;
}

package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class PersonRequest extends PlatformClientRequest {

    private String fullName;
    private LocalDate birthDate;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
}

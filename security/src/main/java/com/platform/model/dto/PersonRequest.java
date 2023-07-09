package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PersonRequest extends PlatformClientRequest {

    private String personUsername;
    private String givenName;
    private String familyName;
    private String middleName;
    private LocalDate birthDate;
}

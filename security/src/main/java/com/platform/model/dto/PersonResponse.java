package com.platform.model.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonResponse extends PlatformClientResponse {

    private String givenName;
    private String familyName;
    private String middleName;
    private LocalDate birthDate;
}

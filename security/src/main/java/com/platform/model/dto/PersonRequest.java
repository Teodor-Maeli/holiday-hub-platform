package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.platform.aspect.Mask;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PersonRequest extends PlatformClientRequest {

    private String personUsername;
    @Mask(personal = true)
    private String familyName;
    @Mask(personal = true)
    private String givenName;
    @Mask(personal = true)
    private String middleName;
    private LocalDate birthDate;
}

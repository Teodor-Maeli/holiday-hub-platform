package com.platform.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LegalEntityResponse extends PlatformClientResponse {

    private String companyName;
    private String companyNumber;
}

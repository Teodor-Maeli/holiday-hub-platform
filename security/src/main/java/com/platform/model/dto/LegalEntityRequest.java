package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class LegalEntityRequest extends PlatformClientRequest {

    private String companyUserName;
    private String companyName;
    private String companyNumber;
    private String contactPerson;
}

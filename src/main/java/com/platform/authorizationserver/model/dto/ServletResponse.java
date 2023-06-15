package com.platform.authorizationserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.platform.authorizationserver.model.PlatformError;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class ServletResponse {

    PlatformClientResponse clientResponse;
    PlatformError[] errors;
}

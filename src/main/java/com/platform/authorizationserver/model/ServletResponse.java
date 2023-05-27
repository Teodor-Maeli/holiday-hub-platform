package com.platform.authorizationserver.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServletResponse {

    PlatformError[] errors;
}

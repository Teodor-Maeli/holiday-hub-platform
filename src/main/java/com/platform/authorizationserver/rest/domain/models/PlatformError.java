package com.platform.authorizationserver.rest.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlatformError {
    private String message;
    private String code;
    private String error;
}

package com.platform.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class SecurityException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;
    private final Throwable cause;
}



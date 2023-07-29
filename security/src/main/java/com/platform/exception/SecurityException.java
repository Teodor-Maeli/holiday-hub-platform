package com.platform.exception;

import com.platform.model.RequestAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class SecurityException extends RuntimeException {

    private final String message;
    private final RequestAction action;
    private final HttpStatus httpStatus;
    private final Throwable throwable;
}



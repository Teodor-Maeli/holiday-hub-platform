package com.platform.handler;

import com.platform.exception.SecurityException;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import org.springframework.http.HttpStatus;

/**
 * 27.05.2023.
 *
 * <p>Handles incoming requests from endpoints - must be implemented by all handlers.</p>
 * {@param com.platform.model.dto.ServletRequest} {@param com.platform.model.RequestAction}
 * <p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
public interface SecurityInvocationHandler<R> {

    R handle(PlatformServletRequest request, RequestAction action);

    void validate(PlatformServletRequest request);

    default void requireNonNull(Object object, String message) {
        if (object == null) {
            throw SecurityException.builder()
                                   .httpStatus(HttpStatus.BAD_REQUEST)
                                   .message(message)
                                   .build();
        }
    }

}

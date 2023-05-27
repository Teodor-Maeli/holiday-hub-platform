package com.platform.authorizationserver.aspect;

import com.platform.authorizationserver.model.HandlerKey;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 27.05.2023.
 *
 * <p>Used to validate handler keys.</p>
 * {@param validKeys}
 * <p>Author : Teodor Maeli</p>
 */

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InvocationValidator {

    HandlerKey[] keys();
}

package com.platform.aspect.logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 27.05.2023.
 *
 * <p>Logs input and output of method invocations..</p>
 * Since 1.0.
 * <p>Author : Teodor Maeli</p>
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IOLogger {

}

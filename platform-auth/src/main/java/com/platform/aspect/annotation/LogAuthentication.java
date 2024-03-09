package com.platform.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAuthentication {


  /**
   * This field is used to instruct to interceptor to log into the database synchronously or asynchronously.
   */
  boolean async();
}

package com.platform.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackAuthentication {

  /**
   * True of logging to be async, false otherwise.
   */
  boolean async();
}

package com.platform.exception;

import org.springframework.http.HttpStatus;

public class PlatformBackendException extends RuntimeException {

  private HttpStatus httpStatus;

  private String message;

  private Throwable cause;

  private PlatformBackendException() {
    super();
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public Throwable getCause() {
    return cause;
  }

  public static PlatformBackendExceptionBuilder builder() {
    return new PlatformBackendExceptionBuilder(new PlatformBackendException());
  }

  public record PlatformBackendExceptionBuilder(PlatformBackendException exception) {

    public PlatformBackendExceptionBuilder httpStatus(HttpStatus httpStatus) {
        this.exception.httpStatus = httpStatus;
        return this;
      }


      public PlatformBackendExceptionBuilder message(String message) {
        this.exception.message = message;
        return this;
      }


      public PlatformBackendExceptionBuilder cause(Throwable cause) {
        this.exception.cause = cause;
        return this;
      }

      public PlatformBackendException build() {
        return this.exception;
      }
    }

}



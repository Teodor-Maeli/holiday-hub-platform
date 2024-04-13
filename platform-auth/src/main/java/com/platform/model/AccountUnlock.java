package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountUnlock(
    com.platform.model.AccountUnlock.State state,
    String redirectUrl,
    String returnUrl,
    String replyTo
) {

  public enum State {
    INITIATED,
    COMPLETED,
    FAILED
  }

}

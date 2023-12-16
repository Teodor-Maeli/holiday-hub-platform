package com.platform.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SessionResponse {

  private String sessionId;
  private String clientUsername;
  private Long clientId;
  private String initiatedDate;
  private String evictedDate;
  private Boolean active;
  private String initiatedBy;
  private String evictedBy;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getClientUsername() {
    return clientUsername;
  }

  public void setClientUsername(String clientUsername) {
    this.clientUsername = clientUsername;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public String getInitiatedDate() {
    return initiatedDate;
  }

  public void setInitiatedDate(String initiatedDate) {
    this.initiatedDate = initiatedDate;
  }

  public String getEvictedDate() {
    return evictedDate;
  }

  public void setEvictedDate(String evictedDate) {
    this.evictedDate = evictedDate;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  public String getEvictedBy() {
    return evictedBy;
  }

  public void setEvictedBy(String evictedBy) {
    this.evictedBy = evictedBy;
  }
}

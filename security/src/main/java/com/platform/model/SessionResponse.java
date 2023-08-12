package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
public class SessionResponse {

    private Long username;
    private String currentSessionId;
    private LocalDateTime currentSessionInitiatedDate;
    private LocalDateTime lastEvictedSessionDate;
    private String lastEvictedSessionId;
    private String lastEvictedSessionComment;
    private String otherComment;

    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
    }

    public String getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(String currentSessionId) {
        this.currentSessionId = currentSessionId;
    }

    public LocalDateTime getCurrentSessionInitiatedDate() {
        return currentSessionInitiatedDate;
    }

    public void setCurrentSessionInitiatedDate(LocalDateTime currentSessionInitiatedDate) {
        this.currentSessionInitiatedDate = currentSessionInitiatedDate;
    }

    public LocalDateTime getLastEvictedSessionDate() {
        return lastEvictedSessionDate;
    }

    public void setLastEvictedSessionDate(LocalDateTime lastEvictedSessionDate) {
        this.lastEvictedSessionDate = lastEvictedSessionDate;
    }

    public String getLastEvictedSessionId() {
        return lastEvictedSessionId;
    }

    public void setLastEvictedSessionId(String lastEvictedSessionId) {
        this.lastEvictedSessionId = lastEvictedSessionId;
    }

    public String getLastEvictedSessionComment() {
        return lastEvictedSessionComment;
    }

    public void setLastEvictedSessionComment(String lastEvictedSessionComment) {
        this.lastEvictedSessionComment = lastEvictedSessionComment;
    }

    public String getOtherComment() {
        return otherComment;
    }

    public void setOtherComment(String otherComment) {
        this.otherComment = otherComment;
    }
}

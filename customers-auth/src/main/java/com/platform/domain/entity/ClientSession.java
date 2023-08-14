package com.platform.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SESSION")
public class ClientSession {

    @Id
    @Column(name = "CLIENT_ID", updatable = false, unique = true)
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "CLIENT_ID")
    private Client client;
    @Column(name = "CURRENT_SESSION_ID")
    private String currentSessionId;
    @Column(name = "LAST_EVICTED_SESSION_ID")
    private String lastEvictedSessionId;
    @Column(name = "LAST_EVICTED_SESSION_COMMENT")
    private String lastEvictedSessionComment;
    @Column(name = "OTHER_COMMENT")
    private String otherComment;
    @Column(name = "CURRENT_SESSION_INITIATED_DATE")
    private LocalDateTime currentSessionInitiatedDate;
    @Column(name = "LAST_EVICTED_SESSION_DATE")
    private LocalDateTime lastEvictedSessionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

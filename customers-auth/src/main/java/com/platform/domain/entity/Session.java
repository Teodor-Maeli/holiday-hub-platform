package com.platform.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "SESSION")
@EntityListeners(AuditingEntityListener.class)
public class Session {

    @Id
    @Column(name = "SESSION_ID", updatable = false, unique = true)
    private String sessionId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;
    @Column(name = "INITIATED_DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime initiatedDate;
    @Column(name = "EVICTED_DATE")
    private LocalDateTime evictedDate;
    @Column(name = "ACTIVE")
    private Boolean active;
    @Column(name = "INITIATED_BY")
    private String initiatedBy;
    @Column(name = "EVICTED_BY")
    private String evictedBy;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getInitiatedDate() {
        return initiatedDate;
    }

    public void setInitiatedDate(LocalDateTime initiatedDate) {
        this.initiatedDate = initiatedDate;
    }

    public LocalDateTime getEvictedDate() {
        return evictedDate;
    }

    public void setEvictedDate(LocalDateTime evictedDate) {
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

package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 27.05.2023.
 *
 * <p>Base response class.</p>
 *
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */

@JsonInclude(Include.NON_NULL)
public class PlatformClientResponse {

    private Long id;
    private String username;
    private String emailAddress;
    private String phoneNumber;
    private Boolean enabled;
    private Boolean premium;
    private LocalDateTime registeredDate;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private SessionResponse session;
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDateTime registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public SessionResponse getSession() {
        return session;
    }

    public void setSession(SessionResponse session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public LocalDateTime getSubscriptionStarts() {
        return subscriptionStarts;
    }

    public void setSubscriptionStarts(LocalDateTime subscriptionStarts) {
        this.subscriptionStarts = subscriptionStarts;
    }

    public LocalDateTime getSubscriptionEnds() {
        return subscriptionEnds;
    }

    public void setSubscriptionEnds(LocalDateTime subscriptionEnds) {
        this.subscriptionEnds = subscriptionEnds;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

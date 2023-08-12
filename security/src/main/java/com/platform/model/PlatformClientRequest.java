package com.platform.model;

import com.platform.aspect.annotation.Mask;
import com.platform.domain.entity.ClientSession;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

/**
 * 27.05.2023.
 *
 * <p>Base dto request class.</p>
 * <p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */

public class PlatformClientRequest {

    private String username;
    @Mask
    private String password;
    @Mask
    private String emailAddress;
    @Mask
    private String phoneNumber;
    private Boolean premium;
    private Boolean enabled;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

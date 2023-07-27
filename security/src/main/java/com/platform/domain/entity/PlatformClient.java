package com.platform.domain.entity;

import com.platform.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 27.05.2023.
 *
 * <p>Base entity class.</p>
 * <p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */

@Getter
@Setter
@MappedSuperclass
public abstract class PlatformClient implements UserDetails {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "REGISTERED_DATE", updatable = false)
    @CreatedDate
    private LocalDate registeredDate;
    @Column(name = "USERNAME", updatable = false)
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ROLES")
    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    private Set<Role> roles;
    @Column(name = "ENABLED")
    private Boolean enabled;
    @Column(name = "PREMIUM")
    private Boolean premium;
    @Column(name = "SUBSCRIPTION_STARTS")
    private LocalDateTime subscriptionStarts;
    @Column(name = "SUBSCRIPTION_ENDS")
    private LocalDateTime subscriptionEnds;
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "MOST_RECENT_SESSION_ID")
    private String mostRecentSessionId;
    @Column(name = "MOST_RECENT_SESSION_INITIATED_DATE")
    private LocalDateTime mostRecentSessionInitiatedDate;
    @Column(name = "LAST_EVICTED_SESSION_DATE")
    private LocalDateTime lastEvictedSessionDate;
    @Column(name = "LAST_EVICTED_SESSION_COMMENT")
    private String lastEvictedSessionComment;
    @Column(name = "OTHER_COMMENT")
    private String otherComment;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(authority -> new SimpleGrantedAuthority(authority.name()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

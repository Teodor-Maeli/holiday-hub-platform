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
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(updatable = false)
    private LocalDate registeredDate;
    @Column(updatable = false)
    private String username;
    @Column
    private String password;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    private Set<Role> roles;
    @Column
    private Boolean isEnabled;
    @Column
    private Boolean isPremiumEnabled;
    @Column
    private LocalDateTime subscriptionStarts;
    @Column
    private LocalDateTime subscriptionEnds;
    @Column
    private String contactEmail;
    @Column
    private String contactPhone;
    @Column
    private String registrationDate;
    @Column
    private String mostRecentSessionId;
    @Column
    private LocalDate mostRecentSessionInitiatedDate;


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
        return this.isEnabled;
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

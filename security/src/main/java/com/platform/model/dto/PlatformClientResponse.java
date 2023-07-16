package com.platform.model.dto;

import com.platform.model.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 27.05.2023.
 *
 * <p>Base dto response class.</p>
 *
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class PlatformClientResponse {

    private String username;
    private Set<Role> roles;
    private Boolean isEnabled;
    private LocalDate registeredDate;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
    private Boolean isPremiumEnabled;
    private String contactEmail;
    private String contactPhone;
    private String mostRecentSessionId;
    private LocalDateTime mostRecentSessionInitiatedDate;
}

package com.platform.authorizationserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.platform.authorizationserver.model.Role;
import java.util.Set;
import lombok.experimental.SuperBuilder;

/**
 * 27.05.2023.
 *
 * <p>Base dto response class.</p>
 *
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public abstract class PlatformClientResponse {

    private String username;
    private Set<Role> roles;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}

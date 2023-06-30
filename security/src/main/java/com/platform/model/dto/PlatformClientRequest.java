package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.platform.model.Role;
import java.util.Set;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 27.05.2023.
 *
 * <p>Base dto request class.</p>
 *
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@Getter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public abstract class PlatformClientRequest {

    private String username;
    private String password;
    private Set<Role> roles;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
}
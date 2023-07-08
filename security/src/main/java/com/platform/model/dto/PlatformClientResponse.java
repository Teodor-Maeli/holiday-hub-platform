package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.platform.model.Role;
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
}

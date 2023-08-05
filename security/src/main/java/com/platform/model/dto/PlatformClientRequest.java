package com.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.platform.aspect.annotation.Mask;
import com.platform.model.Role;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 27.05.2023.
 *
 * <p>Base dto request class.</p>
 * <p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(PersonRequest.class),
    @JsonSubTypes.Type(LegalEntityRequest.class)})
public class PlatformClientRequest {

    private String username;
    @Mask(sensitive = true)
    private String password;
    @Mask(personal = true)
    private String emailAddress;
    @Mask(personal = true)
    private String phone;
    private Set<Role> roles;
    private Boolean enabled;
    private Boolean premiumEnabled;
    private LocalDateTime subscriptionStarts;
    private LocalDateTime subscriptionEnds;
}

package com.platform.authorizationserver.rest.assembler;

import com.platform.authorizationserver.domain.entity.LegalEntity;
import com.platform.authorizationserver.domain.entity.PlatformClient;
import com.platform.authorizationserver.model.Role;
import com.platform.authorizationserver.model.dto.LegalEntityRequest;
import com.platform.authorizationserver.model.dto.LegalEntityResponse;
import com.platform.authorizationserver.model.dto.PlatformClientRequest;
import com.platform.authorizationserver.model.dto.PlatformClientResponse;
import java.util.stream.Collectors;

public class LegalEntityAssembler implements Assembler {

    @Override
    public PlatformClientResponse assemble(PlatformClient entity) {
        LegalEntity legalEntity = (LegalEntity) entity;

        return LegalEntityResponse.builder()
            .username(legalEntity.getUsername())
            .roles(legalEntity.getAuthorities().stream()
                .map(grantedAuthority -> Role.valueOf(grantedAuthority.getAuthority()))
                .collect(Collectors.toSet()))
            .isAccountNonExpired(legalEntity.isAccountNonExpired())
            .isAccountNonLocked(legalEntity.isAccountNonLocked())
            .isCredentialsNonExpired(legalEntity.isCredentialsNonExpired())
            .isEnabled(legalEntity.isEnabled())
            .build();
    }

    @Override
    public PlatformClient assemble(PlatformClientRequest request) {
        LegalEntityRequest legalEntityRequest = (LegalEntityRequest) request;

        return LegalEntity.builder()
            .username(legalEntityRequest.getUsername())
            .password(legalEntityRequest.getPassword())
            .roles(legalEntityRequest.getRoles())
            .isAccountNonExpired(legalEntityRequest.isAccountNonExpired())
            .isCredentialsNonExpired(legalEntityRequest.isCredentialsNonExpired())
            .isAccountNonLocked(legalEntityRequest.isAccountNonLocked())
            .isEnabled(legalEntityRequest.isEnabled())
            .roles(request.getRoles())
            .build();
    }
}

package com.platform.rest.assembler;

import com.platform.domain.entity.LegalEntity;
import com.platform.domain.entity.PlatformClient;
import com.platform.model.dto.LegalEntityRequest;
import com.platform.model.dto.LegalEntityResponse;
import com.platform.model.dto.PlatformClientRequest;
import com.platform.model.dto.PlatformClientResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LegalEntityAssembler implements Assembler {

    private PasswordEncoder encoder;

    @Override
    public PlatformClientResponse assemble(PlatformClient entity) {
        LegalEntity legalEntity = (LegalEntity) entity;
        LegalEntityResponse legalEntityResponse = new LegalEntityResponse();

        legalEntityResponse.setCompanyName(legalEntity.getCompanyName());
        legalEntityResponse.setCompanyNumber(legalEntity.getCompanyNumber());
        legalEntityResponse.setSubscriptionStarts(legalEntity.getSubscriptionStarts());
        legalEntityResponse.setSubscriptionEnds(legalEntity.getSubscriptionEnds());
        legalEntityResponse.setIsPremiumEnabled(legalEntity.getPremium());
        legalEntityResponse.setUsername(legalEntity.getUsername());
        legalEntityResponse.setRoles(legalEntity.getRoles());
        legalEntityResponse.setIsEnabled(legalEntity.isEnabled());
        legalEntityResponse.setMostRecentSessionId(legalEntity.getMostRecentSessionId());
        legalEntityResponse.setMostRecentSessionInitiatedDate(legalEntity.getMostRecentSessionInitiatedDate());
        legalEntityResponse.setRegisteredDate(legalEntity.getRegisteredDate());

        return legalEntityResponse;
    }

    @Override
    public LegalEntity assemble(PlatformClientRequest request) {
        LegalEntityRequest legalEntityRequest = (LegalEntityRequest) request;
        LegalEntity legalEntity = new LegalEntity();

        legalEntity.setCompanyName(legalEntityRequest.getCompanyName());
        legalEntity.setCompanyNumber(legalEntityRequest.getCompanyNumber());
        legalEntity.setSubscriptionStarts(legalEntityRequest.getSubscriptionStarts());
        legalEntity.setSubscriptionEnds(legalEntityRequest.getSubscriptionEnds());
        legalEntity.setPremium(legalEntityRequest.getPremiumEnabled());
        legalEntity.setUsername(legalEntityRequest.getCompanyUserName());
        legalEntity.setPassword(encoder.encode(legalEntityRequest.getPassword()));
        legalEntity.setRoles(legalEntityRequest.getRoles());
        legalEntity.setEnabled(legalEntityRequest.getEnabled());
        legalEntity.setRoles(request.getRoles());
        legalEntity.setEmailAddress(request.getEmailAddress());

        return legalEntity;
    }
}

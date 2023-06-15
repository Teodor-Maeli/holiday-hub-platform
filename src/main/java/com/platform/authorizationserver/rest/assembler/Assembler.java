package com.platform.authorizationserver.rest.assembler;

import com.platform.authorizationserver.domain.entity.PlatformClient;
import com.platform.authorizationserver.model.dto.PlatformClientRequest;
import com.platform.authorizationserver.model.dto.PlatformClientResponse;


public interface Assembler {

    PlatformClientResponse assemble(PlatformClient entity);
    PlatformClient assemble(PlatformClientRequest request);
}

package com.platform.rest.assembler;

import com.platform.domain.entity.PlatformClient;
import com.platform.model.dto.PlatformClientRequest;
import com.platform.model.dto.PlatformClientResponse;

public interface Assembler {

    PlatformClientResponse assemble(PlatformClient entity);
    PlatformClient assemble(PlatformClientRequest request);
}

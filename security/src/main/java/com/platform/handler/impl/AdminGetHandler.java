package com.platform.handler.impl;

import com.platform.handler.SecurityInvocationHandler;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;

public class AdminGetHandler implements SecurityInvocationHandler<PlatformServletResponse> {

    @Override
    public PlatformServletResponse handle(PlatformServletRequest request, RequestAction action) {
        return null;
    }

    @Override
    public void validate(PlatformServletRequest request) {

    }
}

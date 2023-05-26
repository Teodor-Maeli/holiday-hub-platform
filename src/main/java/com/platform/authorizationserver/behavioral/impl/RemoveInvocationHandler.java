package com.platform.authorizationserver.behavioral.impl;

import com.platform.authorizationserver.behavioral.InvocationHandler;
import com.platform.authorizationserver.rest.domain.models.ServletRequest;
import com.platform.authorizationserver.rest.domain.models.ServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RemoveInvocationHandler implements InvocationHandler {


    @Override
    public ServletResponse handle(ServletRequest request) {
        return null;
    }
}

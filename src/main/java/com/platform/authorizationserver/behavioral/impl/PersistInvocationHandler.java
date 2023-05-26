package com.platform.authorizationserver.behavioral.impl;

import com.platform.authorizationserver.behavioral.InvocationHandler;
import com.platform.authorizationserver.rest.domain.model.ServletRequest;
import com.platform.authorizationserver.rest.domain.model.ServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersistInvocationHandler implements InvocationHandler {

    @Override
    public ServletResponse handle(ServletRequest request) {
        return null;
    }
}

package com.platform.authorizationserver.behavioral.impl;

import com.platform.authorizationserver.behavioral.InvocationHandler;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.ServletRequest;
import com.platform.authorizationserver.model.ServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerRegisterInvocationHandler implements InvocationHandler {

    @Override
    public ServletResponse handle(ServletRequest request, HandlerAction action) {
        return null;
    }
}

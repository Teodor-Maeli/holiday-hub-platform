package com.platform.authorizationserver.behavioral.impl;

import com.platform.authorizationserver.behavioral.InvocationHandler;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.dto.ServletRequest;
import com.platform.authorizationserver.model.dto.ServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerEvictInvocationHandler implements InvocationHandler {


    @Override
    public ServletResponse handle(ServletRequest request, HandlerAction action) {
        return null;
    }
}

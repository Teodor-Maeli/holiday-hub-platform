package com.platform.authorizationserver.behavioral;


import com.platform.authorizationserver.rest.domain.model.ServletRequest;
import com.platform.authorizationserver.rest.domain.model.ServletResponse;

public interface InvocationHandler {
    public  ServletResponse handle(ServletRequest request);
}

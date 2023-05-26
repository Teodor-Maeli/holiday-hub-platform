package com.platform.authorizationserver.behavioral;


import com.platform.authorizationserver.rest.domain.models.ServletRequest;
import com.platform.authorizationserver.rest.domain.models.ServletResponse;

public interface InvocationHandler {
    public  ServletResponse handle(ServletRequest request);
}

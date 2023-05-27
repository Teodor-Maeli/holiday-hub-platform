package com.platform.authorizationserver.behavioral;


import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.ServletRequest;
import com.platform.authorizationserver.model.ServletResponse;

/**
 * 27.05.2023.
 *
 * <p>Handles incoming requests from endpoints - must be implemented by all handlers.</p>
 * {@param ServletRequest}
 *
 * <p>Author : Teodor Maeli</p>
 */

public interface InvocationHandler {

    ServletResponse handle(ServletRequest request, HandlerAction action);
}

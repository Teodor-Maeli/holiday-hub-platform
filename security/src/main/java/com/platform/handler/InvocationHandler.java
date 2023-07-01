package com.platform.handler;


import com.platform.model.RequestAction;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;

/**
 * 27.05.2023.
 *
 * <p>Handles incoming requests from endpoints - must be implemented by all handlers.</p>
 * {@param ServletRequest}
 * {@param HandlerAction}
 *
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
public interface InvocationHandler {

    ServletResponse handle(ServletRequest request, RequestAction action);
}

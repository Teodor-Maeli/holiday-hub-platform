package com.platform.handler;

import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;

/**
 * 27.05.2023.
 *
 * <p>Handles incoming requests from endpoints - must be implemented by all handlers.</p>
 * {@param com.platform.model.dto.ServletRequest}
 * {@param com.platform.model.RequestAction}
 *
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
public interface SecurityInvocationHandler {

    PlatformServletResponse handle(PlatformServletRequest request, RequestAction action);
}

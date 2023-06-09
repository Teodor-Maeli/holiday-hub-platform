package com.platform.authorizationserver.behavioral;

import com.platform.authorizationserver.behavioral.impl.AdminEvictInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.AdminFetchInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.AdminRegisterInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.AdminUpdateInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.CustomerEvictInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.CustomerFetchInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.CustomerRegisterInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.CustomerUpdateInvocationHandler;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.HandlerKey;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Returns requested invocation handler
 */
@Service
public class ActionHandlerContext {

    private Map<HandlerKey, InvocationHandler> handlerContext;

    @PostConstruct
    private void init() {
        handlerContext = new EnumMap<>(HandlerKey.class);
        handlerContext.put(HandlerKey.CUSTOMER_EVICT, new CustomerEvictInvocationHandler());
        handlerContext.put(HandlerKey.CUSTOMER_REGISTER, new CustomerRegisterInvocationHandler());
        handlerContext.put(HandlerKey.CUSTOMER_UPDATE, new CustomerUpdateInvocationHandler());
        handlerContext.put(HandlerKey.CUSTOMER_GET, new CustomerFetchInvocationHandler());
        handlerContext.put(HandlerKey.ADMIN_EVICT, new AdminEvictInvocationHandler());
        handlerContext.put(HandlerKey.ADMIN_UPDATE, new AdminUpdateInvocationHandler());
        handlerContext.put(HandlerKey.ADMIN_GET, new AdminFetchInvocationHandler());
        handlerContext.put(HandlerKey.ADMIN_REGISTER, new AdminRegisterInvocationHandler());
    }

    public InvocationHandler getHandler(HandlerAction action) {
        HandlerKey key = getHandlerKeyWithHandlerAction(action);
        return handlerContext.get(key);
    }

    private HandlerKey getHandlerKeyWithHandlerAction(HandlerAction action) {
        return Arrays.stream(HandlerKey.values())
            .filter(handlerKey -> handlerKey.getActions().contains(action))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "No such action supported by Invocation handlers."));
    }
}

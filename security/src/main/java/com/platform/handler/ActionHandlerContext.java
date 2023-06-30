package com.platform.handler;

import com.platform.handler.impl.AdminEvictInvocationHandler;
import com.platform.handler.impl.AdminFetchInvocationHandler;
import com.platform.handler.impl.AdminPersistInvocationHandler;
import com.platform.handler.impl.AdminUpdateInvocationHandler;
import com.platform.handler.impl.CustomerEvictInvocationHandler;
import com.platform.handler.impl.CustomerFetchInvocationHandler;
import com.platform.handler.impl.CustomerPersistInvocationHandler;
import com.platform.handler.impl.CustomerUpdateInvocationHandler;
import com.platform.model.HandlerAction;
import com.platform.model.HandlerKey;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Returns requested invocation handler from application context based on action
 * Since 1.0
 */
@Service
@RequiredArgsConstructor
public class ActionHandlerContext {

    private final ApplicationContext applicationContext;
    private Map<HandlerKey, Class<? extends InvocationHandler>> handlerContext;


    @PostConstruct
    private void init() {
        handlerContext = new EnumMap<>(HandlerKey.class);
        handlerContext.put(HandlerKey.CUSTOMER_EVICT, CustomerEvictInvocationHandler.class);
        handlerContext.put(HandlerKey.CUSTOMER_PERSIST, CustomerPersistInvocationHandler.class);
        handlerContext.put(HandlerKey.CUSTOMER_UPDATE, CustomerUpdateInvocationHandler.class);
        handlerContext.put(HandlerKey.CUSTOMER_GET, CustomerFetchInvocationHandler.class);
        handlerContext.put(HandlerKey.ADMIN_EVICT, AdminEvictInvocationHandler.class);
        handlerContext.put(HandlerKey.ADMIN_UPDATE, AdminUpdateInvocationHandler.class);
        handlerContext.put(HandlerKey.ADMIN_GET, AdminFetchInvocationHandler.class);
        handlerContext.put(HandlerKey.ADMIN_PERSIST, AdminPersistInvocationHandler.class);
    }

    public InvocationHandler getHandler(HandlerAction action) {
        HandlerKey key = getHandlerKeyWithHandlerAction(action);
        Class<? extends InvocationHandler> clazz = handlerContext.get(key);
        return applicationContext.getBean(clazz);
    }

    private HandlerKey getHandlerKeyWithHandlerAction(HandlerAction action) {
        return Arrays.stream(HandlerKey.values())
            .filter(handlerKey -> handlerKey.getActions().contains(action))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "No such action supported by Invocation handlers."));
    }
}
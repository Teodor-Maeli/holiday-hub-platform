package com.platform.handler;

import com.platform.handler.impl.AdminEvictAuthInvocationHandler;
import com.platform.handler.impl.AdminGetAuthInvocationHandler;
import com.platform.handler.impl.AdminPersistAuthInvocationHandler;
import com.platform.handler.impl.AdminUpdateAuthInvocationHandler;
import com.platform.handler.impl.CustomerEvictAuthInvocationHandler;
import com.platform.handler.impl.CustomerGetAuthInvocationHandler;
import com.platform.handler.impl.CustomerPersistAuthInvocationHandler;
import com.platform.handler.impl.CustomerUpdateAuthInvocationHandler;
import com.platform.model.AuthRequestAction;
import com.platform.model.AuthHandlerKey;
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
    private Map<AuthHandlerKey, Class<? extends AuthInvocationHandler>> handlerContext;


    @PostConstruct
    private void init() {
        handlerContext = new EnumMap<>(AuthHandlerKey.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_EVICT, CustomerEvictAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_PERSIST, CustomerPersistAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_UPDATE, CustomerUpdateAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_GET, CustomerGetAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_EVICT, AdminEvictAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_UPDATE, AdminUpdateAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_GET, AdminGetAuthInvocationHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_PERSIST, AdminPersistAuthInvocationHandler.class);
    }

    public AuthInvocationHandler getHandler(AuthRequestAction action) {
        AuthHandlerKey key = getHandlerKeyWithRequestAction(action);
        Class<? extends AuthInvocationHandler> clazz = handlerContext.get(key);
        return applicationContext.getBean(clazz);
    }

    private AuthHandlerKey getHandlerKeyWithRequestAction(AuthRequestAction action) {
        return Arrays.stream(AuthHandlerKey.values())
            .filter(handlerKey -> handlerKey.getActions().contains(action))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "No such action supported by Invocation handlers."));
    }
}

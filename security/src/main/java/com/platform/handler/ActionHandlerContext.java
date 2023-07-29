package com.platform.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.platform.exception.SecurityException;
import com.platform.handler.impl.AdminEvictHandler;
import com.platform.handler.impl.AdminGetHandler;
import com.platform.handler.impl.AdminPersistHandler;
import com.platform.handler.impl.AdminUpdateHandler;
import com.platform.handler.impl.CustomerEvictHandler;
import com.platform.handler.impl.CustomerGetHandler;
import com.platform.handler.impl.CustomerPersistHandler;
import com.platform.handler.impl.CustomerUpdateHandler;
import com.platform.model.AuthHandlerKey;
import com.platform.model.RequestAction;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Returns requested invocation handler from application context based on action Since 1.0
 */
@Service
@RequiredArgsConstructor
public class ActionHandlerContext {

    private final ApplicationContext applicationContext;
    private Map<AuthHandlerKey, Class<? extends SecurityInvocationHandler>> handlerContext;


    @PostConstruct
    private void init() {
        handlerContext = new EnumMap<>(AuthHandlerKey.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_EVICT, CustomerEvictHandler.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_PERSIST, CustomerPersistHandler.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_UPDATE, CustomerUpdateHandler.class);
        handlerContext.put(AuthHandlerKey.CUSTOMER_GET, CustomerGetHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_EVICT, AdminEvictHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_UPDATE, AdminUpdateHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_GET, AdminGetHandler.class);
        handlerContext.put(AuthHandlerKey.ADMIN_PERSIST, AdminPersistHandler.class);
    }

    public SecurityInvocationHandler getHandler(RequestAction action) {
        AuthHandlerKey key = getKeyWithAction(action);
        Class<? extends SecurityInvocationHandler> clazz = handlerContext.get(key);
        return applicationContext.getBean(clazz);
    }

    private AuthHandlerKey getKeyWithAction(RequestAction action) {
        return Arrays.stream(AuthHandlerKey.values())
                     .filter(handlerKey -> handlerKey.getActions().contains(action))
                     .findFirst()
                     .orElseThrow(() -> SecurityException.builder()
                                                         .action(action)
                                                         .httpStatus(BAD_REQUEST)
                                                         .message("No such action supported by Invocation handlers.")
                                                         .build());
    }
}

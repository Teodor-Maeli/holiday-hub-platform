package com.platform.authorizationserver.behavioral;

import com.platform.authorizationserver.behavioral.impl.AdminRemoveInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.AdminUpdateInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.FetchInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.PersistInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.RemoveInvocationHandler;
import com.platform.authorizationserver.behavioral.impl.UpdateInvocationHandler;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Returns requested invocation handler
 */
@Service
public class HandlerContext {

    private Map<HandlerKey, InvocationHandler> context;

    @PostConstruct
    private void init() {
        context = new EnumMap<>(HandlerKey.class);
        context.put(HandlerKey.REMOVE, new RemoveInvocationHandler());
        context.put(HandlerKey.PERSIST, new PersistInvocationHandler());
        context.put(HandlerKey.UPDATE, new UpdateInvocationHandler());
        context.put(HandlerKey.FETCH, new FetchInvocationHandler());
        context.put(HandlerKey.ADMIN_REMOVE, new AdminRemoveInvocationHandler());
        context.put(HandlerKey.ADMIN_UPDATE, new AdminUpdateInvocationHandler());
    }

    public InvocationHandler getHandler(HandlerKey key) {
        return context.get(key);
    }

    public enum HandlerKey {
        REMOVE,
        FETCH,
        UPDATE,
        PERSIST,
        ADMIN_REMOVE,
        ADMIN_UPDATE
    }

}

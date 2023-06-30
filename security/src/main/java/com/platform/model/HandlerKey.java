package com.platform.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 27.05.2023.
 *
 * <p>Invocation handler key , used to determine the handler that is requested.</p>
 * Used also to validate request at controller level using AOP. Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@Getter
@AllArgsConstructor
public enum HandlerKey {

    /**
     * Client/Customer capabilities per HandlerKey for both entity or person. Since 1.0
     */
    CUSTOMER_EVICT(
        List.of(
            HandlerAction.PERSON_DELETE,
            HandlerAction.PERSON_SESSION_INVALIDATE,
            HandlerAction.ENTITY_DELETE,
            HandlerAction.ENTITY_SESSION_INVALIDATE,
            HandlerAction.CLIENT_LOGOUT
        )
    ),
    CUSTOMER_GET(
        List.of(
            HandlerAction.PERSON_FETCH,
            HandlerAction.ENTITY_FETCH,
            HandlerAction.ENTITY_LOGIN,
            HandlerAction.PERSON_LOGIN
        )
    ),
    CUSTOMER_UPDATE(
        List.of(
            HandlerAction.PERSON_UPDATE,
            HandlerAction.ENTITY_UPDATE
        )
    ),
    CUSTOMER_PERSIST(
        List.of(
            HandlerAction.PERSON_REGISTER,
            HandlerAction.ENTITY_REGISTER
        )
    ),

    /**
     * Admin capabilities per HandlerKey. Since 1.0
     */
    ADMIN_UPDATE(
        List.of(
            HandlerAction.ADMIN_PERSON_UPDATE,
            HandlerAction.ADMIN_ENTITY_UPDATE
        )
    ),
    ADMIN_EVICT(
        List.of(
            HandlerAction.ADMIN_PERSON_DELETE,
            HandlerAction.ADMIN_ENTITY_DELETE,
            HandlerAction.ADMIN_PERSON_SESSION_INVALIDATE,
            HandlerAction.ADMIN_ENTITY_SESSION_INVALIDATE,
            HandlerAction.CLIENT_LOGOUT
        )
    ),
    ADMIN_PERSIST(
        List.of(
            HandlerAction.ADMIN_PERSON_REGISTER,
            HandlerAction.ADMIN_ENTITY_REGISTER
        )
    ),
    ADMIN_GET(
        List.of(
            HandlerAction.ADMIN_PERSON_FETCH,
            HandlerAction.ADMIN_ENTITY_FETCH
        )
    );

    private final List<HandlerAction> actions;
}
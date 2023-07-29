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
public enum AuthHandlerKey {

    /**
     * Client/Customer capabilities per HandlerKey for both entity or person. Since 1.0
     */
    CUSTOMER_EVICT(
        List.of(
            RequestAction.PERSON_DELETE,
            RequestAction.PERSON_SESSION_INVALIDATE,
            RequestAction.ENTITY_DELETE,
            RequestAction.ENTITY_SESSION_INVALIDATE,
            RequestAction.CLIENT_LOGOUT
        )
    ),
    CUSTOMER_GET(
        List.of(
            RequestAction.PERSON_FETCH,
            RequestAction.ENTITY_FETCH
        )
    ),
    CUSTOMER_UPDATE(
        List.of(
            RequestAction.PERSON_UPDATE,
            RequestAction.ENTITY_UPDATE
        )
    ),
    CUSTOMER_PERSIST(
        List.of(
            RequestAction.PERSON_REGISTER,
            RequestAction.ENTITY_REGISTER,
            RequestAction.ENTITY_LOGIN,
            RequestAction.PERSON_LOGIN
        )
    ),

    /**
     * Admin capabilities per HandlerKey. Since 1.0
     */
    ADMIN_UPDATE(
        List.of(
            RequestAction.ADMIN_PERSON_UPDATE,
            RequestAction.ADMIN_ENTITY_UPDATE
        )
    ),
    ADMIN_EVICT(
        List.of(
            RequestAction.ADMIN_PERSON_DELETE,
            RequestAction.ADMIN_ENTITY_DELETE,
            RequestAction.ADMIN_PERSON_SESSION_INVALIDATE,
            RequestAction.ADMIN_ENTITY_SESSION_INVALIDATE,
            RequestAction.CLIENT_LOGOUT
        )
    ),
    ADMIN_PERSIST(
        List.of(
            RequestAction.ADMIN_PERSON_REGISTER,
            RequestAction.ADMIN_ENTITY_REGISTER
        )
    ),
    ADMIN_GET(
        List.of(
            RequestAction.ADMIN_PERSON_FETCH,
            RequestAction.ADMIN_ENTITY_FETCH
        )
    );

    private final List<RequestAction> actions;
}
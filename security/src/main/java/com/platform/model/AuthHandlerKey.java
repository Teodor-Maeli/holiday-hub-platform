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
            AuthRequestAction.PERSON_DELETE,
            AuthRequestAction.PERSON_SESSION_INVALIDATE,
            AuthRequestAction.ENTITY_DELETE,
            AuthRequestAction.ENTITY_SESSION_INVALIDATE,
            AuthRequestAction.CLIENT_LOGOUT
        )
    ),
    CUSTOMER_GET(
        List.of(
            AuthRequestAction.PERSON_FETCH,
            AuthRequestAction.ENTITY_FETCH
        )
    ),
    CUSTOMER_UPDATE(
        List.of(
            AuthRequestAction.PERSON_UPDATE,
            AuthRequestAction.ENTITY_UPDATE
        )
    ),
    CUSTOMER_PERSIST(
        List.of(
            AuthRequestAction.PERSON_REGISTER,
            AuthRequestAction.ENTITY_REGISTER,
            AuthRequestAction.ENTITY_LOGIN,
            AuthRequestAction.PERSON_LOGIN
        )
    ),

    /**
     * Admin capabilities per HandlerKey. Since 1.0
     */
    ADMIN_UPDATE(
        List.of(
            AuthRequestAction.ADMIN_PERSON_UPDATE,
            AuthRequestAction.ADMIN_ENTITY_UPDATE
        )
    ),
    ADMIN_EVICT(
        List.of(
            AuthRequestAction.ADMIN_PERSON_DELETE,
            AuthRequestAction.ADMIN_ENTITY_DELETE,
            AuthRequestAction.ADMIN_PERSON_SESSION_INVALIDATE,
            AuthRequestAction.ADMIN_ENTITY_SESSION_INVALIDATE,
            AuthRequestAction.CLIENT_LOGOUT
        )
    ),
    ADMIN_PERSIST(
        List.of(
            AuthRequestAction.ADMIN_PERSON_REGISTER,
            AuthRequestAction.ADMIN_ENTITY_REGISTER
        )
    ),
    ADMIN_GET(
        List.of(
            AuthRequestAction.ADMIN_PERSON_FETCH,
            AuthRequestAction.ADMIN_ENTITY_FETCH
        )
    );

    private final List<AuthRequestAction> actions;
}
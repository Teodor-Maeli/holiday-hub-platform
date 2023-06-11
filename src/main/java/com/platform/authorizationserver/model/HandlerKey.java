package com.platform.authorizationserver.model;

import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_SESSION_INVALIDATE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_SESSION_INVALIDATE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.CLIENT_LOGOUT;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_SESSION_INVALIDATE;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_SESSION_INVALIDATE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_UPDATE;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 27.05.2023.
 *
 * <p>Invocation handler key , used to determine the handler that is requested.</p>
 * Used also to validate request at controller level using AOP.
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@Getter
@AllArgsConstructor
public enum HandlerKey {

    /**
     * Client/Customer capabilities per HandlerKey
     * for both entity or person.
     * Since 1.0
     */
    CUSTOMER_EVICT(
        List.of(
            PERSON_DELETE,
            PERSON_SESSION_INVALIDATE,
            ENTITY_DELETE,
            ENTITY_SESSION_INVALIDATE,
            CLIENT_LOGOUT
        )
    ),
    CUSTOMER_GET(
        List.of(
            PERSON_FETCH,
            ENTITY_FETCH
        )
    ),
    CUSTOMER_UPDATE(
        List.of(
            PERSON_UPDATE,
            ENTITY_UPDATE
        )
    ),
    CUSTOMER_PERSIST(
        List.of(
            PERSON_REGISTER,
            ENTITY_REGISTER
        )
    ),

    /**
     * Admin capabilities per HandlerKey.
     * Since 1.0
     */
    ADMIN_UPDATE(
        List.of(
            ADMIN_PERSON_UPDATE,
            ADMIN_ENTITY_UPDATE
        )
    ),
    ADMIN_EVICT(
        List.of(
            ADMIN_PERSON_DELETE,
            ADMIN_ENTITY_DELETE,
            ADMIN_PERSON_SESSION_INVALIDATE,
            ADMIN_ENTITY_SESSION_INVALIDATE,
            CLIENT_LOGOUT
        )
    ),
    ADMIN_PERSIST(
        List.of(
            ADMIN_PERSON_REGISTER,
            ADMIN_ENTITY_REGISTER
        )
    ),
    ADMIN_GET(
        List.of(
            ADMIN_PERSON_FETCH,
            ADMIN_ENTITY_FETCH
        )
    );

    private final List<HandlerAction> actions;
}
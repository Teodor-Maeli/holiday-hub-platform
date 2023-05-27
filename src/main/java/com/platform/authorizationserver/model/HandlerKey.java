package com.platform.authorizationserver.model;

import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_PERSIST;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_PERSIST;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_PERSIST;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_PERSIST;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_UPDATE;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 27.05.2023.
 *
 * <p>Invocation handler key , used to determine the handler that is requested.</p>
 *
 * <p>Author : Teodor Maeli</p>
 */
@Getter
@AllArgsConstructor
public enum HandlerKey {

    CUSTOMER_DELETE(List.of(PERSON_DELETE, ENTITY_DELETE)),
    CUSTOMER_FETCH(List.of(PERSON_FETCH, ENTITY_FETCH)),
    CUSTOMER_UPDATE(List.of(PERSON_UPDATE, ENTITY_UPDATE)),
    CUSTOMER_PERSIST(List.of(PERSON_PERSIST, ENTITY_PERSIST)),
    ADMIN_UPDATE(List.of(ADMIN_PERSON_UPDATE, ADMIN_ENTITY_UPDATE)),
    ADMIN_DELETE(List.of(ADMIN_PERSON_DELETE, ADMIN_ENTITY_DELETE)),
    ADMIN_PERSIST(List.of(ADMIN_PERSON_PERSIST, ADMIN_ENTITY_PERSIST)),
    ADMIN_FETCH(List.of(ADMIN_PERSON_FETCH, ADMIN_ENTITY_FETCH));

    private final List<HandlerAction> actions;
}
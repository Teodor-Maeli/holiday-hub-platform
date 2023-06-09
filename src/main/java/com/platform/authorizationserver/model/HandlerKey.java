package com.platform.authorizationserver.model;

import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_ENTITY_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.ADMIN_PERSON_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_REGISTER;
import static com.platform.authorizationserver.model.HandlerAction.ENTITY_UPDATE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_DELETE;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_FETCH;
import static com.platform.authorizationserver.model.HandlerAction.PERSON_REGISTER;
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

    CUSTOMER_EVICT(List.of(PERSON_DELETE, ENTITY_DELETE)),
    CUSTOMER_GET(List.of(PERSON_FETCH, ENTITY_FETCH)),
    CUSTOMER_UPDATE(List.of(PERSON_UPDATE, ENTITY_UPDATE)),
    CUSTOMER_REGISTER(List.of(PERSON_REGISTER, ENTITY_REGISTER)),
    ADMIN_UPDATE(List.of(ADMIN_PERSON_UPDATE, ADMIN_ENTITY_UPDATE)),
    ADMIN_EVICT(List.of(ADMIN_PERSON_DELETE, ADMIN_ENTITY_DELETE)),
    ADMIN_REGISTER(List.of(ADMIN_PERSON_REGISTER, ADMIN_ENTITY_REGISTER)),
    ADMIN_GET(List.of(ADMIN_PERSON_FETCH, ADMIN_ENTITY_FETCH));

    private final List<HandlerAction> actions;
}
package com.platform.authorizationserver.model;
/**
 * 27.05.2023.
 *
 * <p>Actions used by invocation handlers.</p>
 *
 * <p>Author : Teodor Maeli</p>
 */
public enum HandlerAction {
    PERSON_REGISTER,
    PERSON_FETCH,
    PERSON_UPDATE,
    PERSON_DELETE,
    PERSON_LOGIN,


    ENTITY_REGISTER,
    ENTITY_FETCH,
    ENTITY_UPDATE,
    ENTITY_DELETE,
    ENTITY_LOGIN,


    ADMIN_PERSON_REGISTER,
    ADMIN_ENTITY_REGISTER,
    ADMIN_PERSON_FETCH,
    ADMIN_ENTITY_FETCH,
    ADMIN_PERSON_UPDATE,
    ADMIN_PERSON_DELETE,
    ADMIN_ENTITY_UPDATE,
    ADMIN_ENTITY_DELETE
}

package com.platform.rest.controller;

import com.platform.aspect.logger.IOLogger;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/customers-auth/v1/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthorizationController {

    @IOLogger
    @PostMapping(path = "/login")
    public ResponseEntity<Void> login(@Param("entity") Entity entity) {
        return null;
    }

    @IOLogger
    @PostMapping(path = "/logout")
    public ResponseEntity<Void> logout(@Param("entity") Entity entity) {
        return null;
    }

    public enum Entity {
        PERSON, COMPANY
    }
}

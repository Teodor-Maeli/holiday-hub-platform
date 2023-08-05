package com.platform.rest.controller;

import static com.platform.model.AuthHandlerKey.ADMIN_EVICT;
import static com.platform.model.AuthHandlerKey.ADMIN_GET;
import static com.platform.model.AuthHandlerKey.ADMIN_PERSIST;
import static com.platform.model.AuthHandlerKey.ADMIN_UPDATE;
import static com.platform.model.AuthHandlerKey.CUSTOMER_EVICT;
import static com.platform.model.AuthHandlerKey.CUSTOMER_GET;
import static com.platform.model.AuthHandlerKey.CUSTOMER_PERSIST;
import static com.platform.model.AuthHandlerKey.CUSTOMER_UPDATE;

import com.platform.aspect.annotation.IOLogger;
import com.platform.aspect.annotation.InvocationValidator;
import com.platform.handler.ActionHandlerContext;
import com.platform.model.RequestAction;
import com.platform.model.dto.PlatformServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 27.05.2023.
 *
 * <p>Controller handles for customer's request.</p>
 * Since 1.0.
 * <p>Author : Teodor Maeli</p>
 */

@RestController
@RequestMapping("/authorization/v1")
@RequiredArgsConstructor
public class PlatformUserAuthController {

    private final ActionHandlerContext context;

    @IOLogger
    @PostMapping("/persist")
    @InvocationValidator(keys = {CUSTOMER_PERSIST, ADMIN_PERSIST})
    public ResponseEntity<Object> processRegister(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        return ResponseEntity.ok().body(context.getHandler(action).handle(request, action));
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = {CUSTOMER_GET, ADMIN_GET})
    public ResponseEntity<Object> processGet(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        return ResponseEntity.ok().body(context.getHandler(action).handle(request, action));
    }

    @IOLogger
    @PostMapping("/update")
    @InvocationValidator(keys = {CUSTOMER_UPDATE, ADMIN_UPDATE})
    public ResponseEntity<Object> processUpdate(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        return ResponseEntity.ok().body(context.getHandler(action).handle(request, action));
    }

    @IOLogger
    @DeleteMapping("/evict")
    @InvocationValidator(keys = {CUSTOMER_EVICT, ADMIN_EVICT})
    public ResponseEntity<Object> processEvict(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        return ResponseEntity.ok().body(context.getHandler(action).handle(request, action));
    }
}

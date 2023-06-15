package com.platform.authorizationserver.rest.controller;

import com.platform.authorizationserver.aspect.IOLogger;
import com.platform.authorizationserver.aspect.InvocationValidator;
import com.platform.authorizationserver.behavioral.ActionHandlerContext;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.HandlerKey;
import com.platform.authorizationserver.model.dto.ServletRequest;
import com.platform.authorizationserver.model.dto.ServletResponse;
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
public class PlatformUserController {

    private final ActionHandlerContext context;


    @IOLogger
    @PostMapping("/persist")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_PERSIST)
    public ResponseEntity<ServletResponse> processRegister(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_GET)
    public ResponseEntity<ServletResponse> processGet(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/update")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_UPDATE)
    public ResponseEntity<ServletResponse> processUpdate(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @DeleteMapping("/evict")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_EVICT)
    public ResponseEntity<ServletResponse> processEvict(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }
}

package com.platform.rest.controller;

import com.platform.aspect.IOLogger;
import com.platform.aspect.InvocationValidator;
import com.platform.handler.ActionHandlerContext;
import com.platform.model.RequestAction;
import com.platform.model.HandlerKey;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
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
    public ResponseEntity<PlatformServletResponse> processRegister(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_GET)
    public ResponseEntity<PlatformServletResponse> processGet(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/update")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_UPDATE)
    public ResponseEntity<PlatformServletResponse> processUpdate(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @DeleteMapping("/evict")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_EVICT)
    public ResponseEntity<PlatformServletResponse> processEvict(
        @RequestBody PlatformServletRequest request,
        @RequestParam RequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }
}

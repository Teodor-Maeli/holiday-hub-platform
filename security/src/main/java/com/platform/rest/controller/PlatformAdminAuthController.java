package com.platform.rest.controller;

import com.platform.aspect.IOLogger;
import com.platform.aspect.InvocationValidator;
import com.platform.handler.ActionHandlerContext;
import com.platform.model.AuthRequestAction;
import com.platform.model.AuthHandlerKey;
import com.platform.model.dto.PlatformServletRequest;
import com.platform.model.dto.PlatformServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 27.05.2023.
 *
 * <p>Controller handles administrator's requests.</p>
 * Since 1.0.
 * <p>Author : Teodor Maeli</p>
 */

@RestController
@RequestMapping("/authorization/admin/v1")
@RequiredArgsConstructor
public class PlatformAdminAuthController {

    private final ActionHandlerContext context;

    @IOLogger
    @PostMapping("/persist")
    @InvocationValidator(keys = AuthHandlerKey.ADMIN_PERSIST)
    public ResponseEntity<PlatformServletResponse> processAdminRegister(
        @RequestBody PlatformServletRequest request,
        @RequestParam AuthRequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = AuthHandlerKey.ADMIN_GET)
    public ResponseEntity<PlatformServletResponse> processAdminGet(
        @RequestBody PlatformServletRequest request,
        @RequestParam AuthRequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PatchMapping("/update")
    @InvocationValidator(keys = AuthHandlerKey.ADMIN_UPDATE)
    public ResponseEntity<PlatformServletResponse> processAdminUpdate(
        @RequestBody PlatformServletRequest request,
        @RequestParam AuthRequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @DeleteMapping("/evict")
    @InvocationValidator(keys = AuthHandlerKey.ADMIN_EVICT)
    public ResponseEntity<PlatformServletResponse> processAdminEvict(
        @RequestBody PlatformServletRequest request,
        @RequestParam AuthRequestAction action) {
        PlatformServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }
}
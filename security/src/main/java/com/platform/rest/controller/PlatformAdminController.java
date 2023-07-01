package com.platform.rest.controller;

import com.platform.aspect.IOLogger;
import com.platform.aspect.InvocationValidator;
import com.platform.handler.ActionHandlerContext;
import com.platform.model.RequestAction;
import com.platform.model.HandlerKey;
import com.platform.model.dto.ServletRequest;
import com.platform.model.dto.ServletResponse;
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
public class PlatformAdminController {

    private final ActionHandlerContext context;

    @IOLogger
    @PostMapping("/persist")
    @InvocationValidator(keys = HandlerKey.ADMIN_PERSIST)
    public ResponseEntity<ServletResponse> processAdminRegister(
        @RequestBody ServletRequest request,
        @RequestParam RequestAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = HandlerKey.ADMIN_GET)
    public ResponseEntity<ServletResponse> processAdminGet(
        @RequestBody ServletRequest request,
        @RequestParam RequestAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PatchMapping("/update")
    @InvocationValidator(keys = HandlerKey.ADMIN_UPDATE)
    public ResponseEntity<ServletResponse> processAdminUpdate(
        @RequestBody ServletRequest request,
        @RequestParam RequestAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @DeleteMapping("/evict")
    @InvocationValidator(keys = HandlerKey.ADMIN_EVICT)
    public ResponseEntity<ServletResponse> processAdminEvict(
        @RequestBody ServletRequest request,
        @RequestParam RequestAction action) {
        ServletResponse result = context.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }
}

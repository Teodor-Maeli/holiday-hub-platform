package com.platform.authorizationserver.rest.controller;

import com.platform.authorizationserver.aspect.IOLogger;
import com.platform.authorizationserver.aspect.InvocationValidator;
import com.platform.authorizationserver.behavioral.HandlerContext;
import com.platform.authorizationserver.model.HandlerAction;
import com.platform.authorizationserver.model.HandlerKey;
import com.platform.authorizationserver.model.ServletRequest;
import com.platform.authorizationserver.model.ServletResponse;
import lombok.AllArgsConstructor;
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
 *
 * <p>Author : Teodor Maeli</p>
 */

@RestController
@RequestMapping("/authorization/v1/admin")
@AllArgsConstructor
public class PlatformAdminController {

    HandlerContext handlerContext;

    @IOLogger
    @PatchMapping("/update")
    @InvocationValidator(keys = HandlerKey.ADMIN_UPDATE)
    public ResponseEntity<ServletResponse> processAdminUpdate(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @DeleteMapping("/delete")
    @InvocationValidator(keys = HandlerKey.ADMIN_DELETE)
    public ResponseEntity<ServletResponse> processAdminDelete(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = HandlerKey.ADMIN_FETCH)
    public ResponseEntity<ServletResponse> processAdminFetch(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/persist")
    @InvocationValidator(keys = HandlerKey.ADMIN_PERSIST)
    public ResponseEntity<ServletResponse> processAdminPersist(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }
}

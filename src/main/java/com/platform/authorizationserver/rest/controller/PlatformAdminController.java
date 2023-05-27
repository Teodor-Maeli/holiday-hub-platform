package com.platform.authorizationserver.rest.controller;

import com.platform.authorizationserver.aspect.InvocationValidator;
import com.platform.authorizationserver.aspect.IOLogger;
import com.platform.authorizationserver.behavioral.HandlerContext;
import com.platform.authorizationserver.behavioral.HandlerContext.HandlerKey;
import com.platform.authorizationserver.rest.domain.model.ServletRequest;
import com.platform.authorizationserver.rest.domain.model.ServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 27.05.2023.
 *
 * <p>Controller responsible for administrator's requests.</p>
 *
 * <p>Author : Teodor Maeli</p>
 */

@RestController
@RequestMapping("/authorization/v1/admin")
@AllArgsConstructor
public class PlatformAdminController {

    HandlerContext handlerContext;

    @IOLogger
    @PostMapping("/update")
    @InvocationValidator(keys = HandlerKey.ADMIN_UPDATE)
    public ResponseEntity<ServletResponse> processAdminUpdate(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandlerByKey(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/fetch")
    @InvocationValidator(keys = HandlerKey.ADMIN_REMOVE)
    public ResponseEntity<ServletResponse> processAdminRemove(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandlerByKey(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

}

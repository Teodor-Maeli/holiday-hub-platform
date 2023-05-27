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
 *
 * <p>Author : Teodor Maeli</p>
 */

@RestController
@RequestMapping("/authorization/v1")
@AllArgsConstructor
public class PlatformUserController {

    private HandlerContext handlerContext;

    @IOLogger
    @PostMapping("/persist")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_PERSIST)
    public ResponseEntity<ServletResponse> processPersist(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/get")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_FETCH)
    public ResponseEntity<ServletResponse> processFetch(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/update")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_UPDATE)
    public ResponseEntity<ServletResponse> processUpdate(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/delete")
    @InvocationValidator(keys = HandlerKey.CUSTOMER_DELETE)
    public ResponseEntity<ServletResponse> processRemove(
        @RequestBody ServletRequest request,
        @RequestParam HandlerAction action) {
        ServletResponse result = handlerContext.getHandler(action).handle(request, action);
        return ResponseEntity.ok().body(result);
    }
}

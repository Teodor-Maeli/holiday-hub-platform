package com.platform.authorizationserver.rest.controller;

import com.platform.authorizationserver.aspect.InvocationValidator;
import com.platform.authorizationserver.aspect.IOLogger;
import com.platform.authorizationserver.behavioral.HandlerContext;
import com.platform.authorizationserver.behavioral.HandlerContext.HandlerKey;
import com.platform.authorizationserver.rest.domain.model.ServletRequest;
import com.platform.authorizationserver.rest.domain.model.ServletResponse;
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
 * <p>Controller responsible for client's request.</p>
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
    @InvocationValidator(keys = HandlerKey.PERSIST)
    public ResponseEntity<ServletResponse> processPersist(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandlerByKey(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @GetMapping("/fetch")
    @InvocationValidator(keys = HandlerKey.FETCH)
    public ResponseEntity<ServletResponse> processFetch(
        @RequestBody ServletRequest request,
        @RequestParam("key") HandlerKey key) {
        ServletResponse result = handlerContext.getHandlerByKey(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/update")
    @InvocationValidator(keys = HandlerKey.UPDATE)
    public ResponseEntity<ServletResponse> processUpdate(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandlerByKey(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @IOLogger
    @PostMapping("/remove")
    @InvocationValidator(keys = HandlerKey.REMOVE)
    public ResponseEntity<ServletResponse> processRemove(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandlerByKey(key).handle(request);
        return ResponseEntity.ok().body(result);
    }
}

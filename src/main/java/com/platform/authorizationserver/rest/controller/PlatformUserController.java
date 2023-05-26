package com.platform.authorizationserver.rest.controller;

import com.platform.authorizationserver.behavioral.HandlerContext;
import com.platform.authorizationserver.behavioral.HandlerContext.HandlerKey;
import com.platform.authorizationserver.rest.domain.model.ServletRequest;
import com.platform.authorizationserver.rest.domain.model.ServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/authorization")
@AllArgsConstructor
public class PlatformUserController {

    private HandlerContext handlerContext;

    //TODO log request response using AOP
    @PostMapping("/persist")
    public ResponseEntity<ServletResponse> processPersist(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandler(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/fetch")
    public ResponseEntity<ServletResponse> processFetch(
        @RequestBody ServletRequest request,
        @RequestParam("key") HandlerKey key) {
        ServletResponse result = handlerContext.getHandler(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/update")
    public ResponseEntity<ServletResponse> processUpdate(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandler(key).handle(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/remove")
    public ResponseEntity<ServletResponse> processRemove(
        @RequestBody ServletRequest request,
        @RequestParam HandlerKey key) {
        ServletResponse result = handlerContext.getHandler(key).handle(request);
        return ResponseEntity.ok().body(result);
    }


}

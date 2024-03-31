package com.platform.rest.controller;

import com.platform.aspect.annotation.IOLogger;
import com.platform.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.AUTH_V_1)
public class AuthController {

  static final String AUTH_V_1 = "/auth/v1";

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @IOLogger
  @PostMapping("/unlock-account/initiate/{username}")
  public ResponseEntity<Void> initiateAccountUnlocking(@PathVariable("username") String username) {
    authService.startAccountUnlocking(username);

    return ResponseEntity
        .noContent()
        .build();
  }

  @IOLogger
  @PostMapping("/unlock-account/complete/{username}")
  public ResponseEntity<Void> completeAccountUnblocking(@PathVariable("username") String username,
                                                        @RequestParam(value = "unlockingCode") String unlockingCode) {
    authService.completeAccountUnlocking(username, unlockingCode);

    return ResponseEntity
        .noContent()
        .build();
  }

}

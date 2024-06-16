package com.platform.rest.controller;

import com.platform.aspect.annotation.IOLogger;
import com.platform.model.AccountUnlock;
import com.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.AUTH_V_1)
@RequiredArgsConstructor
public class AuthController {

  static final String AUTH_V_1 = "/auth/v1";

  private final AuthService authService;

  @IOLogger
  @PostMapping("/unlock-account/initiate/{username}")
  public ResponseEntity<AccountUnlock> initiateAccountUnlocking(
      @PathVariable("username") String username
  ) {
    AccountUnlock response = authService.startAccountUnlocking(username);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }

  @IOLogger
  @PatchMapping("/unlock-account/complete")
  public ResponseEntity<AccountUnlock> completeAccountUnlocking(
      @RequestParam("username") String username,
      @RequestParam(value = "unlockingCode", required = false) String unlockingCode
  ) {
    AccountUnlock response = authService.completeAccountUnlocking(username, unlockingCode);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }

}

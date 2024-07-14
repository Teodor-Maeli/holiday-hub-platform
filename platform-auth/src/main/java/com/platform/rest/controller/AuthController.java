package com.platform.rest.controller;

import com.platform.aspect.audit.Audited;
import com.platform.model.AccountUnlock;
import com.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Audited
@RestController
@RequestMapping("auth/v1")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @ResponseStatus(HttpStatus.ACCEPTED)
  @PostMapping("/unlock-account/initiate/{username}")
  public AccountUnlock initiateAccountUnlocking(@PathVariable String username) {
    return authService.startAccountUnlocking(username);
  }

  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/unlock-account/complete/{username}")
  public AccountUnlock completeAccountUnlocking(@PathVariable String username, @RequestParam (required = false) String unlockingCode) {
    return authService.completeAccountUnlocking(username, unlockingCode);
  }

}

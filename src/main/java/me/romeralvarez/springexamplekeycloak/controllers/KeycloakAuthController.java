package me.romeralvarez.springexamplekeycloak.controllers;

import lombok.AllArgsConstructor;
import me.romeralvarez.springexamplekeycloak.controllers.requests.UserLoginRequest;
import me.romeralvarez.springexamplekeycloak.services.interfaces.KeycloakAuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class KeycloakAuthController {

  private final KeycloakAuthService keycloakAuthService;

  @PostMapping("/login")
  public ResponseEntity<AccessTokenResponse> login(@NonNull @RequestBody UserLoginRequest userLoginRequest) {
   AccessTokenResponse accessTokenResponse = keycloakAuthService.login(userLoginRequest);
    return ResponseEntity.ok().body(accessTokenResponse);
  }

}

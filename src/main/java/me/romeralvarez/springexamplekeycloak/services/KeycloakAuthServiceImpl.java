package me.romeralvarez.springexamplekeycloak.services;

import lombok.extern.slf4j.Slf4j;
import me.romeralvarez.springexamplekeycloak.clients.KeycloakClient;
import me.romeralvarez.springexamplekeycloak.controllers.requests.UserLoginRequest;
import me.romeralvarez.springexamplekeycloak.services.interfaces.KeycloakAuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeycloakAuthServiceImpl implements KeycloakAuthService {
  public AccessTokenResponse login(UserLoginRequest userLoginRequest) {
    return KeycloakClient.getAccessTokenForUser(userLoginRequest.username(), userLoginRequest.password());
  }



}

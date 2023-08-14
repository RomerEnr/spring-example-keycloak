package me.romeralvarez.springexamplekeycloak.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.romeralvarez.springexamplekeycloak.clients.KeycloakClient;
import me.romeralvarez.springexamplekeycloak.dtos.requests.UserLoginRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KeycloakAuthService {
  private KeycloakClient keycloakClient;
  public AccessTokenResponse login(UserLoginRequest userLoginRequest) {
    return keycloakClient.getAccessTokenForUser(userLoginRequest.username(), userLoginRequest.password());
  }


}

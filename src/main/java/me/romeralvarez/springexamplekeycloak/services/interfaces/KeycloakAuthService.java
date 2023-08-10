package me.romeralvarez.springexamplekeycloak.services.interfaces;

import me.romeralvarez.springexamplekeycloak.controllers.requests.UserLoginRequest;
import org.keycloak.representations.AccessTokenResponse;


public interface KeycloakAuthService {
  AccessTokenResponse login(UserLoginRequest userLoginRequest);
}

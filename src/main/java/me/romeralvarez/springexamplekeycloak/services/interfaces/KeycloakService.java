package me.romeralvarez.springexamplekeycloak.services.interfaces;

import me.romeralvarez.springexamplekeycloak.controllers.requests.UserRequest;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {
  List<UserRepresentation> findAllUsers();
  List<UserRepresentation> searchByUsername(String username);
  String createUser(UserRequest userRepresentation);
  void deleteUser(String userId);
  void updateUser(String userId, UserRequest userRepresentation);

}

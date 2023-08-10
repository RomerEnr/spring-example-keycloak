package me.romeralvarez.springexamplekeycloak.services;


import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import me.romeralvarez.springexamplekeycloak.clients.KeycloakClient;
import me.romeralvarez.springexamplekeycloak.controllers.requests.UserRequest;
import me.romeralvarez.springexamplekeycloak.services.interfaces.KeycloakService;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

  @Override
  public List<UserRepresentation> findAllUsers() {
    log.info("Finding all users");
    log.info("Realm: {}", KeycloakClient.getRealmResource().toRepresentation().getRealm());
    return KeycloakClient.getRealmResource().users().list();
  }

  @Override
  public List<UserRepresentation> searchByUsername(String username) {
    return KeycloakClient.getRealmResource().users().searchByUsername(username, true);
  }

  @Override
  public String createUser(@NonNull UserRequest userRequest) {
    log.info("Creating user");
    int status = 0;

    log.info("Realm: {}", KeycloakClient.getRealmResource().toRepresentation().getRealm());
    UsersResource usersResource = KeycloakClient.getUsersResource();
    UserRepresentation userRepresentation = new UserRepresentation();

    log.info("Setting user attributes");
    userRepresentation.setFirstName(userRequest.firstName());
    userRepresentation.setLastName(userRequest.lastName());
    userRepresentation.setEmail(userRequest.email());
    userRepresentation.setUsername(userRequest.username());
    userRepresentation.setEnabled(true);
    userRepresentation.setEmailVerified(true);

    Response response = usersResource.create(userRepresentation);
    status = response.getStatus();

    if (status == 201) {
      log.info("User created");
      String path = response.getLocation().getPath();
      String userId = path.substring(path.lastIndexOf('/') + 1);
      log.info("User id: {}", userId);

      log.info("Setting user password");
      CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
      credentialRepresentation.setTemporary(false);
      credentialRepresentation.setType(OAuth2Constants.PASSWORD);
      credentialRepresentation.setValue(userRequest.password());

      log.info("Resetting user password");
      usersResource.get(userId).resetPassword(credentialRepresentation);

      log.info("Assigning user roles");
      RealmResource realmResource = KeycloakClient.getRealmResource();
      List<RoleRepresentation> roles = null;
      
      if(userRequest.roles() == null || userRequest.roles().isEmpty()){
        roles = List.of(realmResource.roles().get("user").toRepresentation());
      } else {
        roles = realmResource
            .roles()
            .list()
            .stream()
            .filter(role -> userRequest.roles()
                .stream()
                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
            .toList();
      }
      realmResource
          .users()
          .get(userId)
          .roles()
          .realmLevel()
          .add(roles);

      return "User created";
    } else if (status == 409) {
      log.error("User already exists");
      return "User already exists";
    } else {
      log.error("Error creating user");
      return "Error creating user";
    }

  }

  @Override
  public void deleteUser(String userId) {
    log.info("Deleting user");
    log.info("Realm: {}", KeycloakClient.getRealmResource().toRepresentation().getRealm());
    KeycloakClient.getUsersResource().get(userId).remove();
  }

  @Override
  public void updateUser(String userId, UserRequest userRepresentation) {
    log.info("Updating user");
    log.info("Realm: {}", KeycloakClient.getRealmResource().toRepresentation().getRealm());
    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(false);
    credentialRepresentation.setType(OAuth2Constants.PASSWORD);
    credentialRepresentation.setValue(userRepresentation.password());

    UserRepresentation user = new UserRepresentation();
    user.setFirstName(userRepresentation.firstName());
    user.setLastName(userRepresentation.lastName());
    user.setEmail(userRepresentation.email());
    user.setUsername(userRepresentation.username());
    user.setEnabled(true);
    user.setEmailVerified(true);
    user.setCredentials(Collections.singletonList(credentialRepresentation));

    UserResource userResource = KeycloakClient.getUsersResource().get(userId);
    userResource.update(user);
  }
}

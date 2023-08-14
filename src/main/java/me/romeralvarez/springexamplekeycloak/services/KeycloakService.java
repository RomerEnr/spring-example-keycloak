package me.romeralvarez.springexamplekeycloak.services;


import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.romeralvarez.springexamplekeycloak.clients.KeycloakClient;
import me.romeralvarez.springexamplekeycloak.dtos.requests.UserRequest;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class KeycloakService {
  private static final int STATUS_CREATED = 201;
  private static final  int STATUS_CONFLICT = 409;
  private KeycloakClient keycloakClient;

  public List<UserRepresentation> findAllUsers() {
    log.info("Finding all users");
    logRealmInfo();
    return keycloakClient.getRealmResource().users().list();
  }

  public List<UserRepresentation> searchByUsername(String username) {
    return keycloakClient.getRealmResource().users().searchByUsername(username, true);
  }

  public String createUser(@NonNull UserRequest userRequest) {
    log.info("Creating user");
    logRealmInfo();
    Response response = createUserInKeycloak(userRequest);

    if (response.getStatus() == STATUS_CREATED) {
      handleUserCreated(response, userRequest);
      return "User created";
    } else if (response.getStatus() == STATUS_CONFLICT) {
      log.error("User already exists");
      return "User already exists";
    } else {
      log.error("Error creating user");
      return "Error creating user";
    }

  }

  public void deleteUser(String userId) {
    log.info("Deleting user");
    logRealmInfo();
    keycloakClient.getUsersResource().get(userId).remove();
  }

  public void updateUser(String userId, UserRequest userRepresentation) {
    log.info("Updating user");
    logRealmInfo();
    UserResource userResource = keycloakClient.getUsersResource().get(userId);
    userResource.update(createUserRepresentationFromRequest(userRepresentation));
  }

  private void logRealmInfo(){
    log.info("Realm: {}", keycloakClient.getRealmResource().toRepresentation().getRealm());
  }

  private Response createUserInKeycloak(UserRequest userRequest){
    UsersResource userResource = keycloakClient.getUsersResource();
    UserRepresentation userRepresentation = createUserRepresentationFromRequest(userRequest);
    return userResource.create(userRepresentation);
  }

  private UserRepresentation createUserRepresentationFromRequest(UserRequest userRequest){
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setFirstName(userRequest.firstName());
    userRepresentation.setLastName(userRequest.lastName());
    userRepresentation.setEmail(userRequest.email());
    userRepresentation.setUsername(userRequest.username());
    userRepresentation.setEnabled(true);
    userRepresentation.setEmailVerified(true);
    userRepresentation.setCredentials(Collections.singletonList(createCredentialRepresentation(userRequest)));
    return userRepresentation;
  }

  private CredentialRepresentation createCredentialRepresentation(UserRequest userRequest) {
    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
    credentialRepresentation.setTemporary(false);
    credentialRepresentation.setType(OAuth2Constants.PASSWORD);
    credentialRepresentation.setValue(userRequest.password());
    return credentialRepresentation;
  }

  private void handleUserCreated(Response response, UserRequest userRequest) {
    String userId = extractUserIdFromResponse(response);
    log.info("User id: {}", userId);

    setUserPassword(userId, userRequest);
    assignUserRoles(userId, userRequest);
  }

  private String extractUserIdFromResponse(Response response) {
    String path = response.getLocation().getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }

  private void setUserPassword(String userId, UserRequest userRequest) {
    UsersResource usersResource = keycloakClient.getUsersResource();
    usersResource.get(userId).resetPassword(createCredentialRepresentation(userRequest));
  }

  private void assignUserRoles(String userId, UserRequest userRequest) {
    RealmResource realmResource = keycloakClient.getRealmResource();
    List<RoleRepresentation> roles = determineRolesToAssign(userRequest, realmResource);
    realmResource.users().get(userId).roles().realmLevel().add(roles);
  }

  private List<RoleRepresentation> determineRolesToAssign(UserRequest userRequest, RealmResource realmResource) {
    if (userRequest.roles() == null || userRequest.roles().isEmpty()) {
      return List.of(realmResource.roles().get("user").toRepresentation());
    } else {
      return realmResource.roles().list().stream()
          .filter(role -> userRequest.roles().stream().anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
          .collect(Collectors.toList());
    }
  }
}

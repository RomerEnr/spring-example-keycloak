package me.romeralvarez.springexamplekeycloak.clients;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;

import static me.romeralvarez.springexamplekeycloak.commons.Constants.*;

public class KeycloakClient {

  private static final Keycloak keycloakInstance = KeycloakBuilder.builder()
      .serverUrl(KEYCLOAK_URL)
      .realm(REALM_MASTER)
      .clientId(ADMIN_CLI)
      .username(USER_CONSOLE)
      .password(USER_PASSWORD)
      .clientSecret(CLIENT_SECRET)
      .resteasyClient(new ResteasyClientBuilderImpl()
          .connectionPoolSize(10).build())
      .build();

  public static RealmResource getRealmResource() {
    return keycloakInstance.realm(REALM_NAME);
  }

  public static UsersResource getUsersResource() {
    RealmResource realmResource = getRealmResource();
    return realmResource.users();
  }

  public static KeycloakBuilder createKeycloakBuilder(String username, String password) {
    return KeycloakBuilder.builder()
        .realm(REALM_NAME)
        .serverUrl(KEYCLOAK_URL)
        .clientId(CLIENT_ID)
        .clientSecret(CLIENT_SECRET)
        .username(username)
        .password(password);
  }

  public static AccessTokenResponse getAccessTokenForUser(String username, String password) {
    return createKeycloakBuilder(username, password)
        .build()
        .tokenManager()
        .getAccessToken();
  }

}

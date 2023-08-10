package me.romeralvarez.springexamplekeycloak.clients;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.RSATokenVerifier;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;

public class KeycloakClient {
  private static final String KEYCLOAK_URL = "http://localhost:8080/";
  private static final String REALM_NAME = "spring-boot-realm-dev";
  private static final String REALM_MASTER = "master";
  private static final String ADMIN_CLI = "admin-cli";
  private static final String USER_CONSOLE = "admin";
  private static final String USER_PASSWORD = "admin";
  private static final String CLIENT_SECRET = "HGsxBdcgcbBgn5a4I1jWh37GsybL5Vbq";
  private static final String CLIENT_ID = "spring-client-api-rest";

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

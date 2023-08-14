package me.romeralvarez.springexamplekeycloak.clients;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class KeycloakClient {

  @Value("${keycloak.url}")
  private String keycloakUrl;

  @Value("${keycloak.realm-name}")
  private String realmName;

  @Value("${keycloak.realm-master}")
  private String realmMaster;

  @Value("${keycloak.admin-cli}")
  private String adminCli;

  @Value("${keycloak.user-console}")
  private String userConsole;

  @Value("${keycloak.user-password}")
  private String userPassword;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  @Value("${keycloak.client-id}")
  private String clientId;

  private Keycloak keycloakInstance;

  @PostConstruct
  public void init() {
    this.keycloakInstance = createKeycloakInstance();
  }

  protected KeycloakBuilder getKeycloakBuilder() {
    return KeycloakBuilder.builder();
  }

  protected Keycloak createKeycloakInstance() {
    return getKeycloakBuilder()
        .serverUrl(keycloakUrl)
        .realm(realmMaster)
        .clientId(adminCli)
        .username(userConsole)
        .password(userPassword)
        .clientSecret(clientSecret)
        .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
        .build();
  }

  public RealmResource getRealmResource() {
    return keycloakInstance.realm(realmName);
  }

  public UsersResource getUsersResource() {
    RealmResource realmResource = getRealmResource();
    return realmResource.users();
  }

  public KeycloakBuilder createKeycloakBuilder(String username, String password) {
    return getKeycloakBuilder()
        .realm(realmName)
        .serverUrl(keycloakUrl)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .username(username)
        .password(password);
  }

  public AccessTokenResponse getAccessTokenForUser(String username, String password) {
    return createKeycloakBuilder(username, password)
        .build()
        .tokenManager()
        .getAccessToken();
  }
}

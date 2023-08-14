package me.romeralvarez.springexamplekeycloak;

import me.romeralvarez.springexamplekeycloak.clients.KeycloakClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Disabled
class SpringExampleKeycloakApplicationTests {
  @Autowired
  private KeycloakClient keycloakClient;

  @Test
  void contextLoads() {
  }

  @Test
  void testGetRealmResource() {
    RealmResource realmResource = keycloakClient.getRealmResource();
    assertNotNull(realmResource);
  }


}

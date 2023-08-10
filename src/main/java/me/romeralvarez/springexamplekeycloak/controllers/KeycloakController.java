package me.romeralvarez.springexamplekeycloak.controllers;

import lombok.AllArgsConstructor;
import me.romeralvarez.springexamplekeycloak.controllers.requests.UserRequest;
import me.romeralvarez.springexamplekeycloak.services.interfaces.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class KeycloakController {
  private KeycloakService keycloakService;

  @GetMapping("/all")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<?> findAllUsers() {
    return ResponseEntity.ok(keycloakService.findAllUsers());
  }

  @GetMapping("/user/{username}")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<?> searchByUsername(@PathVariable String username) {
    return ResponseEntity.ok(keycloakService.searchByUsername(username));
  }

  @PostMapping("/user/create")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) throws URISyntaxException {
    String response = keycloakService.createUser(userRequest);
    return ResponseEntity.created(new URI("/api/users/user/create")).body(response);
  }

  @PutMapping("/user/update/{userId}")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserRequest userRequest) {
    keycloakService.updateUser(userId, userRequest);
    return ResponseEntity.ok("User updated successfully");
  }
}

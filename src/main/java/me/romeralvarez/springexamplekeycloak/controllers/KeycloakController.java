package me.romeralvarez.springexamplekeycloak.controllers;

import lombok.AllArgsConstructor;
import me.romeralvarez.springexamplekeycloak.dtos.requests.UserRequest;
import me.romeralvarez.springexamplekeycloak.dtos.responses.ApiResponse;
import me.romeralvarez.springexamplekeycloak.services.KeycloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class KeycloakController {
  private KeycloakService keycloakService;

  @GetMapping("/all")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<List<UserRepresentation>> findAll() {
    return ResponseEntity.ok(keycloakService.findAllUsers());
  }

  @GetMapping("/{username}")
  @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
  public ResponseEntity<UserRepresentation> searchByUsername(@PathVariable String username) {
    return ResponseEntity.ok(keycloakService.searchByUsername(username).get(0));
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest userRequest) throws URISyntaxException {
    String userId = keycloakService.createUser(userRequest);
    URI location = new URI("/api/users/" + userId);
    return ResponseEntity.created(location).body(new ApiResponse("User created successfully", HttpStatus.CREATED.value()));
  }

  @PutMapping("/update/{userId}")
  @PreAuthorize("hasRole('admin_client_role')")
  public ResponseEntity<ApiResponse> updateUser(@PathVariable String userId, @RequestBody UserRequest userRequest) {
    keycloakService.updateUser(userId, userRequest);
    return ResponseEntity.ok(new ApiResponse("User updated successfully", HttpStatus.OK.value()));
  }
}

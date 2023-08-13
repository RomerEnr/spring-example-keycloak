package me.romeralvarez.springexamplekeycloak.controllers.requests;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserRequest(String username, String email, String firstName, String lastName, String password,
                          Set<String> roles) {
}

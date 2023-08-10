package me.romeralvarez.springexamplekeycloak.controllers.requests;

public record UserLoginRequest(String username, String password) {
}

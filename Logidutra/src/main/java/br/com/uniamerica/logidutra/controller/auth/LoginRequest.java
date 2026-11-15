package br.com.uniamerica.logidutra.controller.auth;

public record LoginRequest(
        String login,
        String senha
) {
}

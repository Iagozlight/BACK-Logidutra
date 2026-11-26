package br.com.uniamerica.logidutra.controller.auth;

import br.com.uniamerica.logidutra.enums.Role;

public record LoginResponse(String token,
                            Long id,
                            String nome,
                            Role role) {
}
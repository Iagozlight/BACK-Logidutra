package br.com.uniamerica.Logidutra.controller.dto;

import javax.management.relation.Role;

public record UsuarioRequest(
        String nome,
        String senha,
        Integer idade,
        Role role
) {



}

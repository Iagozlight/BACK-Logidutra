package br.com.uniamerica.logidutra.controller.dto;


import br.com.uniamerica.logidutra.enums.Role;

public record UsuarioRequest(
        String nome,
        String senha,
        Integer idade,
        Role role
) {



}

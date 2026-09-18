package br.com.uniamerica.Logidutra.controller.dto;


import br.com.uniamerica.Logidutra.enums.Role;

public record UsuarioRequest(
        String nome,
        String senha,
        Integer idade,
        Role role
) {



}

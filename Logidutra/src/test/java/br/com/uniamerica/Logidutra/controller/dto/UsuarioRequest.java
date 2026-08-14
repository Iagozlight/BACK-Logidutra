package br.com.uniamerica.Logidutra.controller.dto;

public record UsuarioRequest(
        long id,
        String nome,
        String senha,
        Integer idade,
        Boolean perm
) {



}

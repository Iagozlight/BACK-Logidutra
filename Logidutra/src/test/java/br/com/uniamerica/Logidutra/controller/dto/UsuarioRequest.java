package br.com.uniamerica.Logidutra.controller.dto;

public record UsuarioRequest(
        String nome,
        String senha,
        Integer idade,
        Boolean perm
) {



}

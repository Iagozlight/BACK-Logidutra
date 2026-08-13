package br.com.uniamerica.Logidutra.dto;

public record UsuarioRequest(
        long id,
        String nome,
        String senha,
        Integer idade,
        Boolean perm
) {

}

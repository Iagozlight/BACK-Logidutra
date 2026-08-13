package br.com.uniamerica.Logidutra.dto;

import br.com.uniamerica.Logidutra.entity.Usuario;

public record UsuarioResponse(
        long id,
        String nome,
        String senha,
        Integer idade,
        Boolean perm
) {


    public static UsuarioResponse de (Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSenha(),
                usuario.getIdade(),
                usuario.getPerm()
        );
    }
}

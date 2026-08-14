package br.com.uniamerica.Logidutra.controller.dto;

import br.com.uniamerica.Logidutra.entity.Usuario;
import jakarta.validation.constraints.NotBlank;

//@NotBlank é uma validação, ele bloqueia nulls, ""vazios, e "    " espaços vazios.

public record UsuarioResponse(
        long id,
        @NotBlank(message = "o nome e obrigatorio")
        String nome,
        @NotBlank(message = "a senha e obrigatorio")
        String senha,
        @NotBlank(message = "a idade e obrigatorio")
        Integer idade,
        @NotBlank(message = "permissao obrigatorio")
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

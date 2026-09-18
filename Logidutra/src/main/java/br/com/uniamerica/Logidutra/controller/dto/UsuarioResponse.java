package br.com.uniamerica.Logidutra.controller.dto;

import br.com.uniamerica.Logidutra.entity.Usuario;

import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


//@NotBlank é uma validação, ele bloqueia nulls, ""vazios, e "    " espaços vazios.

public record UsuarioResponse(
        long id,
        @NotBlank(message = "o nome é obrigatório")
        String nome,
        @NotNull
        Integer idade,
        Role role,
        StatusOperacional status
) {


    public static UsuarioResponse de (Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getIdade(),
                usuario.getRole(),
                usuario.getStatus()
        );
    }
}

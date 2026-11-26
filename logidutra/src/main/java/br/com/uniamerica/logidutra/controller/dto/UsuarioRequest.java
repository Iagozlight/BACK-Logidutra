package br.com.uniamerica.logidutra.controller.dto;


import br.com.uniamerica.logidutra.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @NotBlank(message = "Nome obrigatório.")
        String nome,
        @NotBlank(message = "Senha é obrigatório")
        String senha,
        Integer idade,
        @NotBlank(message = "O login é obrigatório")
        String login,
        @NotNull(message = "O cargo é obrigatório.")
        Role role
) {



}

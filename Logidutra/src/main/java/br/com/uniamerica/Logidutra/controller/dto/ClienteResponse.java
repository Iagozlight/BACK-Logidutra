package br.com.uniamerica.Logidutra.controller.dto;

import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import jakarta.validation.constraints.NotBlank;

public record ClienteResponse(
        long id,
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank
        String cpf,
        @NotBlank
        String telefone,
        @NotBlank(message = "O CEP é obrigatório")
        String cep
) {

    public static ClienteResponse de (ClienteEntity clienteEntity) {
        return new ClienteResponse(
                clienteEntity.getId(),
                clienteEntity.getNome(),
                clienteEntity.getCpf(),
                clienteEntity.getTelefone(),
                clienteEntity.getCep()
        );
    }
}

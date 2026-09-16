package br.com.uniamerica.Logidutra.controller.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotBlank
        String telefone,
        @NotBlank
        String cep,
        @NotBlank
        String logradouro,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade
) {
}
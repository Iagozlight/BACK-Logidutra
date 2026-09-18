package br.com.uniamerica.Logidutra.controller.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequest(
        @NotBlank
        String nome,
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos 000.000.000-00")
        String cpf,
        @NotBlank
        String telefone,
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
        String cep
) {
}
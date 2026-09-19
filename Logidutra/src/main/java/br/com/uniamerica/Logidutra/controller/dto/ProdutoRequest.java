package br.com.uniamerica.Logidutra.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequest(
        Long id,
        @NotBlank(message = "Digite um nome para o produto")
        String nome,
        @NotNull(message = "Digite um valor para o produto")
        @Positive(message = "O valor deve ser maior que zero")
        Double preco
) {
}
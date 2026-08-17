package br.com.uniamerica.Logidutra.dto.veiculos;

import jakarta.validation.constraints.NotBlank;

public record VeiculoRequest(
    @NotBlank
    String marca,
    @NotBlank
    String modelo,
    @NotBlank
    String placa
) {
}

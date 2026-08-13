package br.com.uniamerica.Logidutra.dto.veiculos;

public record VeiculoRequest(
    long id,
    String marca,
    String modelo,
    String placa
) {
}

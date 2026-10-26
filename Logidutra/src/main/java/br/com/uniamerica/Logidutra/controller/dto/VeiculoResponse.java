package br.com.uniamerica.logidutra.controller.dto;

import br.com.uniamerica.logidutra.entity.VeiculoEntity;

public record VeiculoResponse(
    long id,
    String marca,
    String modelo,
    String placa
) {
    public static VeiculoResponse de(VeiculoEntity veiculo){
        return new VeiculoResponse(
            veiculo.getId(), 
            veiculo.getMarca(), 
            veiculo.getModelo(), 
            veiculo.getPlaca());
    }
}

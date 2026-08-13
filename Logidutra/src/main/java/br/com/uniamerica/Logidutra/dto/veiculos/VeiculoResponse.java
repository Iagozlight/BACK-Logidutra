package br.com.uniamerica.Logidutra.dto.veiculos;

import br.com.uniamerica.Logidutra.entity.VeiculoEntity;

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

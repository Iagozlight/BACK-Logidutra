package br.com.uniamerica.Logidutra.controller.dto;

import br.com.uniamerica.Logidutra.entity.Romaneios;

import java.time.LocalDate;
import java.util.List;

public record RomaneiosResponse (
        Long id,
        LocalDate data,
        VeiculoResponse veiculo,
        UsuarioResponse motorista,
        List<ClienteResponse> clientes,
        List<ProdutoResponse> produtos
){

    public static RomaneiosResponse de (Romaneios romaneios){
        return new RomaneiosResponse(
                romaneios.getId(),
                romaneios.getData(),
                VeiculoResponse.de(romaneios.getVeiculo()),
                UsuarioResponse.de(romaneios.getUsuario()),
                romaneios.getClientes().stream().map(ClienteResponse::de).toList(),
                romaneios.getProdutoList() == null
                        ? List.of()
                        : romaneios.getProdutoList().stream().map(ProdutoResponse::de).toList()
        );
    }
}

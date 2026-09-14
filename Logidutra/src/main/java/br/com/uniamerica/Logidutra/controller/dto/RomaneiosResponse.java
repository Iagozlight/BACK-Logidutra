package br.com.uniamerica.Logidutra.controller.dto;

import br.com.uniamerica.Logidutra.entity.Romaneios;

import java.time.LocalDate;

public record RomaneiosResponse (Long id,
                                 LocalDate data
){

    public static RomaneiosResponse de (Romaneios romaneios){
        return new RomaneiosResponse(
                romaneios.getId(),
                romaneios.getData()
        );
    }
}

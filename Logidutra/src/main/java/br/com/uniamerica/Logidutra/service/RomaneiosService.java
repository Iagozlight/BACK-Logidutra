package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.repository.RomaneiosRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RomaneiosService {

    private final RomaneiosRepository romaneiosRepository;

    public Romaneios salvar(RomaneiosRequest romaneiosRequest){

    }
}

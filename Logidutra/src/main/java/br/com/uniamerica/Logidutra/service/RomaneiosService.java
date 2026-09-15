package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.repository.RomaneiosRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class RomaneiosService {

    private final RomaneiosRepository romaneiosRepository;

    private Logger logger;

    public Romaneios salvar(RomaneiosRequest romaneiosRequest){

        log.info("Iniciando a validação de um romaneio");
        Romaneios romaneiosnovo = new Romaneios();

        romaneiosnovo.setId(romaneiosRequest.id());
        romaneiosnovo.setData(romaneiosRequest.data());

        log.info("romaneio aprovado e salvo");

        try {
            log.error("Teste");
        }catch (Exception e){

        }

        return this.romaneiosRepository.save(romaneiosnovo);

    }
}

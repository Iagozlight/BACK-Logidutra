package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.Logidutra.entity.Produto;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.repository.RomaneiosRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
        return this.romaneiosRepository.save(romaneiosnovo);
    }

    public List <Romaneios> listar (){
        return this.romaneiosRepository.findAll();
    }

    public Romaneios buscarPorId (long id){
        return this.romaneiosRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
                );
    }

    public Romaneios atualizar (long id, RomaneiosRequest romaneiosRequest) {
        Romaneios romaneios = this.buscarPorId(id);

        romaneios.setData(romaneiosRequest.data());

        return this.romaneiosRepository.save(romaneios);
    }

    public Romaneios atualizarParcial (long id, RomaneiosRequest romaneiosRequest){
        Romaneios romaneios = this.buscarPorId(id);

        if(romaneiosRequest.data()!= null) romaneios.setData(romaneiosRequest.data());

        return this.romaneiosRepository.save(romaneios);
    }

    public void deletarPorID(long id){
        this.buscarPorId(id);
        this.romaneiosRepository.deleteById(id);
    }
}

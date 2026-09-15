package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoResponse;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.service.RomaneiosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/logidutra/romaneios")
@Slf4j
public class RomaneiosController {

    @Autowired
    private RomaneiosService romaneiosService;

    @PostMapping("/salvar")
    public ResponseEntity<RomaneiosResponse> salvar (@Valid @RequestBody RomaneiosRequest romaneiosRequest){
        log.info("End-point /salvar: Requisição de salvamento de romaneio");
        try {

            Romaneios romaneios = this.romaneiosService.salvar(romaneiosRequest);
            return new ResponseEntity<RomaneiosResponse>(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

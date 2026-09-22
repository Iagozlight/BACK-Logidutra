package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.controller.dto.ProdutoResponse;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.Logidutra.entity.Produto;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.service.RomaneiosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/logidutra/romaneios")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class RomaneiosController {

    @Autowired
    private RomaneiosService romaneiosService;

    @PostMapping("/salvar")
    public ResponseEntity<RomaneiosResponse> salvar(@Valid @RequestBody RomaneiosRequest romaneiosRequest){
        try{
            Romaneios romaneios = this.romaneiosService.salvar(romaneiosRequest);
            return new ResponseEntity<RomaneiosResponse>(RomaneiosResponse.de(romaneios), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<RomaneiosResponse>> listar(){
        try{
            List<RomaneiosResponse> romaneiosList =
                    this.romaneiosService.listar()
                            .stream()
                            .map(RomaneiosResponse::de)
                            .toList();
            return new ResponseEntity<>(romaneiosList, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<RomaneiosResponse> buscarPorId(@PathVariable long id){
        try {
            Romaneios romaneios = this.romaneiosService.buscarPorId(id);
            return new ResponseEntity(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<RomaneiosResponse> atualizar(
            @RequestParam(required = true) long id, @Valid @RequestBody RomaneiosRequest romaneiosRequest) {
        try {
            Romaneios romaneios = romaneiosService.atualizar(id, romaneiosRequest);
            return new ResponseEntity<RomaneiosResponse>(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/atualizar")
    public ResponseEntity<RomaneiosResponse> atualizarParcial(
            @RequestParam(required = true) long id, @Valid @RequestBody RomaneiosRequest romaneiosRequest){
        try {
            Romaneios romaneios = this.romaneiosService.atualizarParcial(id, romaneiosRequest);
            return new ResponseEntity<RomaneiosResponse>(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletar(@PathVariable long id){
        try {
            this.romaneiosService.deletarPorID(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

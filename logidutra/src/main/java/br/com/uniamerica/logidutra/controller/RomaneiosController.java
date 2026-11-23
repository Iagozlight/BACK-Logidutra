package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.logidutra.entity.Romaneios;
import br.com.uniamerica.logidutra.service.RomaneiosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/logidutra/romaneios")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class RomaneiosController {

    @Autowired
    private RomaneiosService romaneiosService;

    @PostMapping
    public ResponseEntity<RomaneiosResponse> salvar(@Valid @RequestBody RomaneiosRequest romaneiosRequest){
        try{
            Romaneios romaneios = this.romaneiosService.salvar(romaneiosRequest);
            return new ResponseEntity<>(RomaneiosResponse.de(romaneios), HttpStatus.CREATED);
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

    @GetMapping("/{id}")
    public ResponseEntity<RomaneiosResponse> buscarPorId(@PathVariable long id){
        try {
            Romaneios romaneios = this.romaneiosService.buscarPorId(id);
            return new ResponseEntity<>(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RomaneiosResponse> atualizar(
            @RequestParam(required = true) long id, @Valid @RequestBody RomaneiosRequest romaneiosRequest) {
        try {
            Romaneios romaneios = romaneiosService.atualizar(id, romaneiosRequest);
            return new ResponseEntity<>(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<RomaneiosResponse> atualizarParcial(
            @RequestParam(required = true) long id, @Valid @RequestBody RomaneiosRequest romaneiosRequest){
        try {
            Romaneios romaneios = this.romaneiosService.atualizarParcial(id, romaneiosRequest);
            return new ResponseEntity<>(RomaneiosResponse.de(romaneios), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            this.romaneiosService.deletarPorID(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
package br.com.uniamerica.Logidutra.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.uniamerica.Logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.Logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.service.VeiculoService;
import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/logidutra/veiculo")
@AllArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<VeiculoResponse> salvar(@RequestBody @Valid VeiculoRequest veiculoRequest) {

        try {
            VeiculoEntity veiculo = veiculoService.salvar(veiculoRequest);
            return new ResponseEntity<VeiculoResponse>(VeiculoResponse.de(veiculo), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> listar() {
        try {
            List<VeiculoResponse> lista = veiculoService.listar().stream().map(item -> VeiculoResponse.de(item)).collect(Collectors.toList());
            return new ResponseEntity<List<VeiculoResponse>>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<VeiculoResponse>>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.buscarPorId(id);
            return new ResponseEntity<VeiculoResponse>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping
    public ResponseEntity<VeiculoResponse> atualizarVeiculo(@RequestParam(required = true) Long id, @RequestBody @Valid VeiculoRequest veiculoRequest) {
        try {
            VeiculoEntity veiculo = veiculoService.atualizar(id, veiculoRequest);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping
    public ResponseEntity<VeiculoResponse> atualizarParcialVeiculo(@RequestParam(required = true) Long id, @RequestBody @Valid VeiculoRequest veiculoRequest) {
        try {
            VeiculoEntity veiculo = veiculoService.atualizarParcial(id, veiculoRequest);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping("/{id}/em-rota")
    public ResponseEntity<VeiculoResponse> marcarEmRota(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarEmRota(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PatchMapping("/{id}/disponivel")
    public ResponseEntity<VeiculoResponse> marcarDisponivel(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarDisponivel(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VeiculoResponse> deletar(@PathVariable Long id) {
        try {
            veiculoService.deletar(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }
}

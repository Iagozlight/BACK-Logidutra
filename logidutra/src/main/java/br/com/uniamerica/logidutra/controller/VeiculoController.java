package br.com.uniamerica.logidutra.controller;

import java.util.List;

import br.com.uniamerica.logidutra.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.uniamerica.logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.logidutra.entity.VeiculoEntity;
import br.com.uniamerica.logidutra.service.VeiculoService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/logidutra/veiculo")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeiculoResponse> salvar(@RequestBody @Valid VeiculoRequest veiculoRequest, @AuthenticationPrincipal Usuario usuarioLogado) {

        try {
            VeiculoEntity veiculo = veiculoService.salvar(veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> listar() {
        try {
            List<VeiculoResponse> lista = veiculoService.listar().stream().map(VeiculoResponse::de).toList();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.buscarPorId(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeiculoResponse> atualizarVeiculo(@RequestParam Long id, @RequestBody @Valid VeiculoRequest veiculoRequest,@AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            VeiculoEntity veiculo = veiculoService.atualizar(id, veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeiculoResponse> atualizarParcialVeiculo(@RequestParam Long id, @RequestBody VeiculoRequest veiculoRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            VeiculoEntity veiculo = veiculoService.atualizarParcial(id, veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping("/{id}/em-rota")
    public ResponseEntity<VeiculoResponse> marcarEmRota(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarEmRota(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PatchMapping("/{id}/disponivel")
    public ResponseEntity<VeiculoResponse> marcarDisponivel(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarDisponivel(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeiculoResponse> deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            veiculoService.deletar(id, usuarioLogado);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }
}

package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.logidutra.entity.ClienteEntity;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/logidutra/cliente")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> salvar(@RequestBody @Valid ClienteRequest clienteRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            ClienteEntity cliente = clienteService.salvar(clienteRequest, usuarioLogado);
            return new ResponseEntity<>(ClienteResponse.de(cliente), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        try {
            List<ClienteResponse> lista = clienteService.listar()
                    .stream()
                    .map(ClienteResponse::de)
                    .toList();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        try {
            ClienteEntity cliente = clienteService.buscarPorId(id);
            return new ResponseEntity<>(ClienteResponse.de(cliente), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> atualizar(@RequestParam Long id, @RequestBody @Valid ClienteRequest clienteRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            ClienteEntity cliente = clienteService.atualizar(id, clienteRequest, usuarioLogado);
            return new ResponseEntity<>(ClienteResponse.de(cliente), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> atualizarParcial(@RequestParam Long id, @RequestBody ClienteRequest clienteRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            ClienteEntity cliente = clienteService.atualizarParcial(id, clienteRequest, usuarioLogado);
            return new ResponseEntity<>(ClienteResponse.de(cliente), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            clienteService.deletar(id, usuarioLogado);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}

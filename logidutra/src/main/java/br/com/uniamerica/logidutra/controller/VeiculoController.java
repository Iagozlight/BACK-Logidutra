package br.com.uniamerica.logidutra.controller;

import java.util.List;

import br.com.uniamerica.logidutra.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import br.com.uniamerica.logidutra.exception.ConflitoDeRotaException;
import br.com.uniamerica.logidutra.exception.PlacaInvalidaException;
import br.com.uniamerica.logidutra.exception.RegistroNaoEncontradoException;
import br.com.uniamerica.logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
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
    public ResponseEntity<?> salvar(
            @RequestBody @Valid VeiculoRequest veiculoRequest,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            VeiculoEntity veiculo = veiculoService.salvar(veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.CREATED);
        } catch (PlacaInvalidaException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Formato de placa invalido!");

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Erro ao salvar registro no banco de dados!");
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e){
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
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
          
            VeiculoEntity veiculo = veiculoService.buscarPorId(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar Veiculo!");
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long id, @RequestBody @Valid VeiculoRequest veiculoRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            VeiculoEntity veiculo = veiculoService.atualizar(id, veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (PlacaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Formato de placa invalido!");
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi possivel encontrar Veiculo!");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarParcialVeiculo(@PathVariable Long id, @RequestBody @Valid VeiculoRequest veiculoRequest, @AuthenticationPrincipal Usuario usuarioLogado) {

        try {
            VeiculoEntity veiculo = veiculoService.atualizarParcial(id, veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);

        } catch (PlacaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de placa invalido!");
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar Veiculo!");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping("/{id}/em-rota")
    public ResponseEntity<?> marcarEmRota(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarEmRota(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        }catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado no banco de dados");

        } catch(ConflitoDeRotaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário já está em Rota!");

        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar registro no banco de dados!");

        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getStatusCode());

        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
          }
        }

    @PatchMapping("/{id}/disponivel")
    public ResponseEntity<?> marcarDisponivel(@PathVariable Long id) {
        try{
            VeiculoEntity veiculo = veiculoService.marcarDisponivel(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);

        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado no banco de dados");
        }catch (ConflitoDeRotaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Veículo já está em Rota!");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar registro no banco de dados!");
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            veiculoService.deletar(id, usuarioLogado);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado no banco de dados");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir registro no banco de dados!");
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


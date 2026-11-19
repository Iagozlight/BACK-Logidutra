package br.com.uniamerica.logidutra.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.exception.ConflitoDeRotaException;
import br.com.uniamerica.logidutra.exception.PlacaInvalidaException;
import br.com.uniamerica.logidutra.exception.RegistroNaoEncontradoException;
import br.com.uniamerica.logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.uniamerica.logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.logidutra.entity.VeiculoEntity;
import br.com.uniamerica.logidutra.service.VeiculoService;
import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/logidutra/veiculo")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VeiculoController {

    private final VeiculoService veiculoService;

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid VeiculoRequest veiculoRequest, @RequestParam Long usuarioLogadoId) {

        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            VeiculoEntity veiculo = veiculoService.salvar(veiculoRequest, usuarioLogado);
            return new ResponseEntity<VeiculoResponse>(VeiculoResponse.de(veiculo), HttpStatus.CREATED);

        } catch (PlacaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de placa invalido!");
        } catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
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
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.buscarPorId(id);
            return new ResponseEntity<VeiculoResponse>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar Veiculo!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long id, @RequestBody @Valid VeiculoRequest veiculoRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            VeiculoEntity veiculo = veiculoService.atualizar(id, veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (PlacaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de placa invalido!");
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar Veiculo!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcialVeiculo(@RequestParam(required = true) Long id, @RequestBody @Valid VeiculoRequest veiculoRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            VeiculoEntity veiculo = veiculoService.atualizarParcial(id, veiculoRequest, usuarioLogado);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (PlacaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de placa invalido!");
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar Veiculo!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PatchMapping("/{id}/em-rota")
    public ResponseEntity<?> marcarEmRota(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarEmRota(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado no banco de dados");
        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        } catch(ConflitoDeRotaException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Usuario já está em Rota!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/disponivel")
    public ResponseEntity<?> marcarDisponivel(@PathVariable Long id) {
        try {
            VeiculoEntity veiculo = veiculoService.marcarDisponivel(id);
            return new ResponseEntity<>(VeiculoResponse.de(veiculo), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado no banco de dados");
        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        } catch(ConflitoDeRotaException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Usuario já está em Rota!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            veiculoService.deletar(id, usuarioLogado);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!!");
        } catch (RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado no banco de dados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

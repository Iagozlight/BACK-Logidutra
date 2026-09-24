package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/logidutra/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid UsuarioRequest usuarioRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            Usuario usuario = this.usuarioService.salvar(usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestParam String nome, @RequestParam String senha) {
        try {
            Usuario usuario = this.usuarioService.login(nome, senha);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        try {
            List<UsuarioResponse> usuarioList =
                    this.usuarioService.listar()
                            .stream()
                            .map(UsuarioResponse::de)
                            .toList();

            return new ResponseEntity<>(usuarioList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable long id) {
        try {
            Usuario usuario = this.usuarioService.buscarPorId(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



    @PutMapping
    public ResponseEntity<UsuarioResponse> atualizar(
            @RequestParam(required = true) long id, @RequestBody @Valid UsuarioRequest usuarioRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            Usuario usuario = usuarioService.atualizar(id, usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping
    public ResponseEntity<UsuarioResponse> atualizarParcial(
            @RequestParam(required = true) long id, @RequestBody @Valid UsuarioRequest usuarioRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            Usuario usuario = usuarioService.atualizarParcial(id, usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/recuperar-senha")
    public ResponseEntity<?> redefinirSenha(@RequestParam String nome, @RequestParam String novaSenha) {
        try {
            usuarioService.redefinirSenha(nome, novaSenha);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao redefinir senha.");
        }
    }

    @PatchMapping("/{id}/em-rota")
    public ResponseEntity<UsuarioResponse> marcarEmRota(@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.marcarEmRota(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/disponivel")
    public ResponseEntity<UsuarioResponse> marcarDisponivel(@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.marcarDisponivel(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            usuarioService.deletarPorId(id, usuarioLogado);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

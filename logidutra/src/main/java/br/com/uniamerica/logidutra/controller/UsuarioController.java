package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.exception.ConflitoDeRotaException;
import br.com.uniamerica.logidutra.exception.RegistroExistenteException;
import br.com.uniamerica.logidutra.exception.RegistroNaoEncontradoException;
import br.com.uniamerica.logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
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
        } catch (RegistroExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
    public ResponseEntity<?> listar() {
        try {
            List<UsuarioResponse> usuarioList =
                    this.usuarioService.listar()
                            .stream()
                            .map(UsuarioResponse::de)
                            .toList();

            return new ResponseEntity<>(usuarioList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable long id) {
        try {
            Usuario usuario = this.usuarioService.buscarPorId(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }

    }



    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable long id, @RequestBody @Valid UsuarioRequest usuarioRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            Usuario usuario = usuarioService.atualizar(id, usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado no banco de dados");
        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(
            @PathVariable long id, @RequestBody @Valid UsuarioRequest usuarioRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            Usuario usuario = usuarioService.atualizarParcial(id, usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado no banco de dados");
        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
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
    public ResponseEntity<?> marcarEmRota(@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.marcarEmRota(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
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
    public ResponseEntity<?> marcarDisponivel(@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.marcarDisponivel(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
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
    public ResponseEntity<?> deletar(@PathVariable long id, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = this.usuarioService.buscarPorId(usuarioLogadoId);
            usuarioService.deletarPorId(id, usuarioLogado);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!!");
        } catch (RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado no banco de dados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/logidutra/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<UsuarioResponse> salvar(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuario = this.usuarioService.salvar(usuarioRequest);
            return new ResponseEntity<UsuarioResponse>(UsuarioResponse.de(usuario), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping()
    public ResponseEntity<List<UsuarioResponse>> listar() {
        try {
            List<UsuarioResponse> usuarioList =
                    this.usuarioService.listar()
                            .stream()
                            .map(UsuarioResponse::de)
                            .toList();

            return new ResponseEntity(usuarioList, HttpStatus.OK);
        } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable long id) {
        try {
            Usuario usuario = this.usuarioService.buscarPorId(id);
            return new ResponseEntity(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable long id, @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuario = usuarioService.atualizar(id, usuarioRequest);
            return new ResponseEntity<UsuarioResponse>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }


    @PatchMapping("{id}")
    public ResponseEntity<UsuarioResponse> atualizarParcial(
            @PathVariable long id, @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuario = usuarioService.atualizarParcial(id, usuarioRequest);
            return new ResponseEntity<UsuarioResponse>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable long id) {
        try {
            this.usuarioService.deletarPorId(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}

package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.service.ClienteService;
import br.com.uniamerica.Logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/logidutra/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ClienteResponse> salvar(@RequestBody @Valid ClienteRequest clienteRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            ClienteEntity clienteEntity = clienteService.salvar(clienteRequest, usuarioLogado);
            return new ResponseEntity<>(ClienteResponse.de(clienteEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        try {
            List<ClienteResponse> clienteList =
                    clienteService.listar()
                            .stream()
                            .map(ClienteResponse::de)
                            .toList();

            return new ResponseEntity<>(clienteList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        try {
            ClienteEntity clienteEntity = clienteService.buscarPorId(id);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(NoSuchElementException e){
            return new ResponseEntity<ClienteResponse>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<ClienteResponse>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ClienteResponse> atualizar(@RequestParam(required=true) Long id, @RequestBody @Valid ClienteRequest clienteRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            ClienteEntity clienteEntity = clienteService.atualizar(id, clienteRequest, usuarioLogado);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(NoSuchElementException e){
            return new ResponseEntity<ClienteResponse>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<ClienteResponse>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping
    public ResponseEntity<ClienteResponse> atualizarParcial(@RequestParam(required = true) Long id, @RequestBody @Valid ClienteRequest clienteRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            ClienteEntity clienteEntity = clienteService.atualizarParcial(id, clienteRequest, usuarioLogado);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(NoSuchElementException e){
            return new ResponseEntity<ClienteResponse>(HttpStatus.NOT_FOUND);
        } catch(Exception e){
            return new ResponseEntity<ClienteResponse>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @RequestParam Long usuarioLogadoId) {
        try{
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            clienteService.deletar(id, usuarioLogado);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.service.ClienteService;
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

    @PostMapping
    public ResponseEntity<ClienteResponse> salvar(@RequestBody @Valid ClienteRequest clienteRequest) {
        try {
            ClienteEntity clienteEntity = this.clienteService.salvar(clienteRequest);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        try {
            List<ClienteResponse> clienteList =
                    this.clienteService.listar()
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
    public ResponseEntity<ClienteResponse> atualizar(@RequestParam(required=true) Long id, @Valid ClienteRequest clienteRequest) {
        try {
            ClienteEntity clienteEntity = clienteService.atualizar(id, clienteRequest);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(NoSuchElementException e){
            return new ResponseEntity<ClienteResponse>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<ClienteResponse>(HttpStatus.BAD_REQUEST);
        }
    }

}

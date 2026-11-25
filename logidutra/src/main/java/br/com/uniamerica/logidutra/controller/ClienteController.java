package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.logidutra.entity.ClienteEntity;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.exception.ApiExternaIndisponivelException;
import br.com.uniamerica.logidutra.exception.CepInvalidoException;
import br.com.uniamerica.logidutra.exception.RegistroNaoEncontradoException;
import br.com.uniamerica.logidutra.service.ClienteService;
import br.com.uniamerica.logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logidutra/cliente")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid ClienteRequest clienteRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            ClienteEntity clienteEntity = clienteService.salvar(clienteRequest, usuarioLogado);
            return new ResponseEntity<>(ClienteResponse.de(clienteEntity), HttpStatus.CREATED);

        } catch(ApiExternaIndisponivelException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Não foi possivel comunicar com a API viaCep!");
        }catch(CepInvalidoException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cep com formato Inválido");
        } catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<ClienteResponse> clienteList =
                    clienteService.listar()
                            .stream()
                            .map(ClienteResponse::de)
                            .toList();

            return new ResponseEntity<>(clienteList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            ClienteEntity clienteEntity = clienteService.buscarPorId(id);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar cliente!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest clienteRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            ClienteEntity clienteEntity = clienteService.atualizar(id, clienteRequest, usuarioLogado);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(ApiExternaIndisponivelException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Não foi possivel comunicar com a API viaCep!");
        }catch(CepInvalidoException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cep com formato Inválido");
        } catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody @Valid ClienteRequest clienteRequest, @RequestParam Long usuarioLogadoId) {
        try {
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            ClienteEntity clienteEntity = clienteService.atualizarParcial(id, clienteRequest, usuarioLogado);
            return new ResponseEntity<ClienteResponse>(ClienteResponse.de(clienteEntity), HttpStatus.OK);
        } catch(ApiExternaIndisponivelException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Não foi possivel comunicar com a API viaCep!");
        }catch(CepInvalidoException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cep com formato Inválido");
        } catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Erro ao salvar registro no banco de dados!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id, @RequestParam Long usuarioLogadoId) {
        try{
            Usuario usuarioLogado = usuarioService.buscarPorId(usuarioLogadoId);
            clienteService.deletar(id, usuarioLogado);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch(RegistroNaoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possivel encontrar cliente!");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }

}

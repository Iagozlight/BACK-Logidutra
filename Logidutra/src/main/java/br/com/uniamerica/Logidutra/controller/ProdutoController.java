package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.controller.dto.ProdutoResponse;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.entity.Produto;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.repository.ProdutoRepository;
import br.com.uniamerica.Logidutra.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/logidutra/produto")
@CrossOrigin(origins = "http://localhost:4200")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/salvar")
    public ResponseEntity<ProdutoResponse> salvar(@Valid @RequestBody ProdutoRequest produtoRequest){
        try{
            Produto produto = this.produtoService.salvar(produtoRequest);
            return new ResponseEntity<ProdutoResponse>(ProdutoResponse.de(produto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar(){
        try{
            List<ProdutoResponse> produtoList =
                    this.produtoService.listar()
                            .stream()
                            .map(ProdutoResponse::de)
                            .toList();

            return new ResponseEntity<>(produtoList, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable long id){
        try {
            Produto produto = this.produtoService.buscarPorId(id);
            return new ResponseEntity(ProdutoResponse.de(produto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ProdutoResponse> atualizar(
            @RequestParam(required = true) long id, @RequestBody ProdutoRequest produtoRequest) {
        try {
            Produto produto = produtoService.atualizar(id, produtoRequest);
            return new ResponseEntity<ProdutoResponse>(ProdutoResponse.de(produto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/atualizar")
    public ResponseEntity<ProdutoResponse> atualizarParcial(
            @RequestParam(required = true) long id, @RequestBody ProdutoRequest produtoRequest){
        try {
            Produto produto = this.produtoService.atualizarParcial(id, produtoRequest);
            return new ResponseEntity<ProdutoResponse>(ProdutoResponse.de(produto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletar(@PathVariable long id){
        try {
            this.produtoService.deletarPorID(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.logidutra.controller.dto.ProdutoResponse;
import br.com.uniamerica.logidutra.entity.Produto;
import br.com.uniamerica.logidutra.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/logidutra/produto")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponse> salvar(@Valid @RequestBody ProdutoRequest produtoRequest){
        try{
            Produto produto = this.produtoService.salvar(produtoRequest);
            return new ResponseEntity<>(ProdutoResponse.de(produto), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
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
            return new ResponseEntity<>(ProdutoResponse.de(produto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/atualizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponse> atualizar(
            @RequestParam() long id, @RequestBody ProdutoRequest produtoRequest) {
        try {
            Produto produto = produtoService.atualizar(id, produtoRequest);
            return new ResponseEntity<>(ProdutoResponse.de(produto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/atualizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponse> atualizarParcial(
            @RequestParam() long id, @RequestBody ProdutoRequest produtoRequest){
        try {
            Produto produto = this.produtoService.atualizarParcial(id, produtoRequest);
            return new ResponseEntity<>(ProdutoResponse.de(produto), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable long id){
        try {
            this.produtoService.deletarPorID(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

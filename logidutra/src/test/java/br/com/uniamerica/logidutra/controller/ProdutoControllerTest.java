package br.com.uniamerica.logidutra.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import br.com.uniamerica.logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.logidutra.controller.dto.ProdutoResponse;
import br.com.uniamerica.logidutra.entity.Produto;
import br.com.uniamerica.logidutra.repository.ProdutoRepository;

@SpringBootTest 
public class ProdutoControllerTest {
    @Autowired 
    ProdutoController produtoController;

    @MockitoBean
    ProdutoRepository produtoRepository;

    @Test 
    void ProdutoControllerSalvarRetornaCreated(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.save(any())).thenReturn(produtoSalvo);

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.salvar(new ProdutoRequest(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getPreco()));

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerSalvarBadRequestComDadosInvalidos(){
        ResponseEntity<ProdutoResponse> resultado = this.produtoController.salvar(new ProdutoRequest(null, null, null));
        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());

        verify(produtoRepository, never()).save(any());
    }

    @Test 
    void ProdutoControllerSalvarRetornaOK(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerSalvarRetornaBadRequest(){
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.buscarPorId(1L);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerListarRetornaOK(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findAll()).thenReturn(List.of(produtoSalvo));

        ResponseEntity<List<ProdutoResponse>> resultado = this.produtoController.listar();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerListarRetornaBadRequest(){
        when(produtoRepository.findAll()).thenThrow(new IllegalArgumentException());

        ResponseEntity<List<ProdutoResponse>> resultado = this.produtoController.listar();

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerAtualizarRetornaOK(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));
        when(produtoRepository.save(any())).thenReturn(produtoSalvo);

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.atualizar(1L,new ProdutoRequest(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getPreco()));

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerAtualizarBadRequest(){
        when(produtoRepository.findAll()).thenThrow(new IllegalArgumentException());

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.atualizar(1L,new ProdutoRequest(1L, "", 10.0));

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerAtualizarParcialRetornaOK(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));
        when(produtoRepository.save(any())).thenReturn(produtoSalvo);

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.atualizarParcial(1L,new ProdutoRequest(null, produtoSalvo.getNome(), null));

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerAtualizarParcialBadRequest(){
        when(produtoRepository.findAll()).thenThrow(new IllegalArgumentException());

        ResponseEntity<ProdutoResponse> resultado = this.produtoController.atualizarParcial(1L,new ProdutoRequest(1L, "", 10.0));

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerDeletarRetornaOK(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));

        ResponseEntity resultado = this.produtoController.deletar(1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test 
    void ProdutoControllerDeletarRetornaBadRequest(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity resultado = this.produtoController.deletar(1L);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }
}

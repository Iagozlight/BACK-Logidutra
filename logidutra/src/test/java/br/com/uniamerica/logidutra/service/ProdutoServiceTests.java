package br.com.uniamerica.logidutra.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import br.com.uniamerica.logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.logidutra.entity.Produto;
import br.com.uniamerica.logidutra.repository.ProdutoRepository;

@SpringBootTest 
public class ProdutoServiceTests {
    @Autowired 
    ProdutoService produtoService;
    @MockitoBean 
    ProdutoRepository produtoRepository;

    @Test 
    void ProdutoServiceSalvarRetornaSucesso(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.save(any())).thenReturn(produtoSalvo);

        var resultado = this.produtoService.salvar(new ProdutoRequest(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getPreco()));

        assertEquals(produtoSalvo.getId(), resultado.getId());
        assertEquals(produtoSalvo.getNome(), resultado.getNome());
        assertEquals(produtoSalvo.getPreco(), resultado.getPreco());
    }
    @Test
    void ProdutoServiceBuscarPorIdRetornaProduto(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));

        var resultado = this.produtoService.buscarPorId(1L);

        assertEquals(produtoSalvo.getId(), resultado.getId());
        assertEquals(produtoSalvo.getNome(), resultado.getNome());
        assertEquals(produtoSalvo.getPreco(), resultado.getPreco());
    }
    @Test
    void ProdutoServiceBuscarPorIdLancaExcessaoComIdInexistente(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> this.produtoService.buscarPorId(1L));
    }

    @Test
    void ProdutoServiceAtualizarRetornaSucesso(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.save(any())).thenReturn(produtoSalvo);
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));

        var resultado = this.produtoService.atualizar(1L,new ProdutoRequest(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getPreco()));

        assertEquals(produtoSalvo.getId(), resultado.getId());
        assertEquals(produtoSalvo.getNome(), resultado.getNome());
        assertEquals(produtoSalvo.getPreco(), resultado.getPreco());
    }

    @Test
    void ProdutoServiceAtualizarParcialNomeRetornaSucesso(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));
        when(produtoRepository.save(any())).thenReturn(produtoSalvo);

        var resultado = this.produtoService.atualizarParcial(1L, new ProdutoRequest(null, "Berco", null));

        assertEquals(produtoSalvo.getId(), resultado.getId());
        assertEquals("Berco", resultado.getNome());
        assertEquals(produtoSalvo.getPreco(), resultado.getPreco());
    }
    @Test
    void ProdutoServiceAtualizarParcialPrecoRetornaSucesso(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.save(any())).thenReturn(produtoSalvo);
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));

        var resultado = this.produtoService.atualizarParcial(1L, new ProdutoRequest(null, null, 15.0));

        assertEquals(produtoSalvo.getId(), resultado.getId());
        assertEquals(produtoSalvo.getNome(), resultado.getNome());
        assertEquals(15, resultado.getPreco());
    }
    @Test 
    void ProdutoServiceDeleteChamaDelete(){
        Produto produtoSalvo =  new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Teste");
        produtoSalvo.setPreco(10.0);

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produtoSalvo));

        this.produtoService.deletarPorID(1L);

        verify(produtoRepository).deleteById(1L);
    }
}

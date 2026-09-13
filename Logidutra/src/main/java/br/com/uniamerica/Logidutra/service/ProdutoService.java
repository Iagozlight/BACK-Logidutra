package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Produto;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto salvar (ProdutoRequest produtoRequest){
        Produto produto1 = new Produto();
        produto1.setId(produtoRequest.id());
        produto1.setNome(produtoRequest.nome());
        produto1.setPreco(produtoRequest.preco());
        return this.produtoRepository.save(produto1);

    }

    public List<Produto> listar(){
        return this.produtoRepository.findAll();
    }

    public Produto buscarPorId(long id){
        return this.produtoRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto" + "Não encontrado")
                );
    }

    public Produto atualizar(long id, ProdutoRequest produtoRequest) {
        Produto produtoatualizado = this.buscarPorId(id);

        produtoatualizado.setNome(produtoRequest.nome());
        produtoatualizado.setPreco(produtoRequest.preco());


        return this.produtoRepository.save(produtoatualizado);
    }

    public Produto atualizarParcial (long id, ProdutoRequest produtoRequest){
        Produto produto = this.buscarPorId(id);

        if(produtoRequest.nome()!= null) produto.setNome(produtoRequest.nome());
        if(produtoRequest.preco()!= null) produto.setPreco(produtoRequest.preco());

        return this.produtoRepository.save(produto);
    }

    public void deletarPorID(long id){
        this.buscarPorId(id);
        this.produtoRepository.deleteById(id);
    }
}

package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.entity.Produto;
import br.com.uniamerica.Logidutra.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}

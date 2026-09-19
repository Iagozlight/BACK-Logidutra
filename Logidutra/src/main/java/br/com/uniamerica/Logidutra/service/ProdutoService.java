package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Produto;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto salvar (ProdutoRequest produtoRequest){
        log.info("Iniciando o cadastro de um novo produto: {}", produtoRequest.nome());

        Produto produto1 = new Produto();
        produto1.setId(produtoRequest.id());
        produto1.setNome(produtoRequest.nome());
        produto1.setPreco(produtoRequest.preco());

        Produto salvo = this.produtoRepository.save(produto1);
        log.info("Produto salvo com sucesso. ID: {}", salvo.getId());
        return salvo;
    }

    public List<Produto> listar(){
        log.info("Listando todos os produtos");
        return this.produtoRepository.findAll();
    }

    public Produto buscarPorId(long id){
        log.info("Buscando produto por ID: {}", id);
        return this.produtoRepository.findById(id)
                .orElseThrow(
                        () -> {
                            log.warn("Produto não encontrado. ID: {}", id);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Produto" + "Não encontrado");
                        }
                );
    }

    public Produto atualizar(long id, ProdutoRequest produtoRequest) {
        log.info("Iniciando atualização completa do produto. ID: {}", id);
        Produto produtoatualizado = this.buscarPorId(id);

        produtoatualizado.setNome(produtoRequest.nome());
        produtoatualizado.setPreco(produtoRequest.preco());

        Produto salvo = this.produtoRepository.save(produtoatualizado);
        log.info("Produto atualizado com sucesso. ID: {}", salvo.getId());
        return salvo;
    }

    public Produto atualizarParcial (long id, ProdutoRequest produtoRequest){
        log.info("Iniciando atualização parcial do produto. ID: {}", id);
        Produto produto = this.buscarPorId(id);

        if(produtoRequest.nome()!= null) produto.setNome(produtoRequest.nome());
        if(produtoRequest.preco()!= null) produto.setPreco(produtoRequest.preco());

        Produto salvo = this.produtoRepository.save(produto);
        log.info("Produto atualizado parcialmente com sucesso. ID: {}", salvo.getId());
        return salvo;
    }

    public void deletarPorID(long id){
        log.info("Iniciando exclusão do produto. ID: {}", id);
        this.buscarPorId(id);
        this.produtoRepository.deleteById(id);
        log.info("Produto excluído com sucesso. ID: {}", id);
    }
}
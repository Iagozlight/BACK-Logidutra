package br.com.uniamerica.Logidutra.controller.dto;

import br.com.uniamerica.Logidutra.entity.Produto;
import jakarta.validation.constraints.NotBlank;

public record ProdutoResponse(
        long id,
        String nome,
        Double preco
) {

    public static ProdutoResponse de (Produto produto){
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco()
        );
    }
}

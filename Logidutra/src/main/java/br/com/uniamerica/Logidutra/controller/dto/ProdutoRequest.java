package br.com.uniamerica.Logidutra.controller.dto;

public record ProdutoRequest(
        Long Id,
        String nome,
        Double preco
) {

}

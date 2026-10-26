package br.com.uniamerica.logidutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.uniamerica.logidutra.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

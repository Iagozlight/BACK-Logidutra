package br.com.uniamerica.logidutra.repository;

import br.com.uniamerica.logidutra.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

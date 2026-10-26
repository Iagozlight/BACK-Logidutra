package br.com.uniamerica.Logidutra.repository;

import br.com.uniamerica.Logidutra.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

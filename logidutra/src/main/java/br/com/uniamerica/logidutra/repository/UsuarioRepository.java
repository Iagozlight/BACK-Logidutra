package br.com.uniamerica.logidutra.repository;

import br.com.uniamerica.logidutra.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNome(String nome);
    Usuario findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);

}

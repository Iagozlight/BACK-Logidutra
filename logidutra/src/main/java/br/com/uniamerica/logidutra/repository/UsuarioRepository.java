package br.com.uniamerica.logidutra.repository;

import br.com.uniamerica.logidutra.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNome(String nome);
    Usuario findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByLogin(String login);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
    boolean existsByLoginAndIdNot(String login, Long id);
    Optional<Usuario> findByLogin(String login);

}

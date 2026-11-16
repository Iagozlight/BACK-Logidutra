package br.com.uniamerica.logidutra.repository;

import br.com.uniamerica.logidutra.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNome(String nome);
    Usuario findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);

    Usuario usuario = new Usuario();

    static Optional<Usuario> findByLogin(String login) {
        if (usuario.getLogin().equals(login)) {
            return Optional.of(usuario);
        }
        return Optional.empty();
    }

}

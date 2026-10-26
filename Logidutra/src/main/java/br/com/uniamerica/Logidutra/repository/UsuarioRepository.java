package br.com.uniamerica.logidutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.uniamerica.logidutra.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByNome(String nome);
    Usuario findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);

}

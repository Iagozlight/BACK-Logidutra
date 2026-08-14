package br.com.uniamerica.Logidutra.repository;

import br.com.uniamerica.Logidutra.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}

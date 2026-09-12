package br.com.uniamerica.Logidutra.repository;

import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}

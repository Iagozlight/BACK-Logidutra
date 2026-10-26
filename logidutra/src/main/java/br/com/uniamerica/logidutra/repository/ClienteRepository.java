package br.com.uniamerica.logidutra.repository;

import br.com.uniamerica.logidutra.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}

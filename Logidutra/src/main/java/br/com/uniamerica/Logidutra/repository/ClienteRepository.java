package br.com.uniamerica.logidutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.uniamerica.logidutra.entity.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}

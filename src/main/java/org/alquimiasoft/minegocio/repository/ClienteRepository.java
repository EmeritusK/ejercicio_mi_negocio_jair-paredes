package org.alquimiasoft.minegocio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.alquimiasoft.minegocio.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);
}

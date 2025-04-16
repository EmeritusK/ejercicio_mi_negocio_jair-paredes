
package org.alquimiasoft.minegocio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.alquimiasoft.minegocio.entity.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByClienteId(Long clienteId);
    long countByClienteIdAndMatrizTrue(Long clienteId);
}

package org.alquimiasoft.minegocio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.alquimiasoft.minegocio.entity.Cliente;
import org.springframework.data.domain.Page;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);

    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(c.numeroIdentificacion) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    Page<Cliente> buscarPorFiltro(@Param("filtro") String filtro, Pageable pageable);
}

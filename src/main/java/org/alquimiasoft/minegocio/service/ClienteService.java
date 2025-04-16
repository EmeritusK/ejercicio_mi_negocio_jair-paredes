
package org.alquimiasoft.minegocio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.alquimiasoft.minegocio.dto.ClienteDTO;

// OCP: Permite extender el comportamiento del servicio sin modificar esta interfaz.
public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO dto);
    ClienteDTO editarCliente(Long id, ClienteDTO dto);
    void eliminarCliente(Long id);
    Page<ClienteDTO> buscarClientes(String filtro, Pageable pageable);
}

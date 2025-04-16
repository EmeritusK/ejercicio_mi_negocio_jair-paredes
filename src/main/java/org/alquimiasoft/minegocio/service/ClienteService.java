
package org.alquimiasoft.minegocio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.alquimiasoft.minegocio.dto.ClienteDTO;

public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO dto);
    ClienteDTO editarCliente(Long id, ClienteDTO dto);
    void eliminarCliente(Long id);
    Page<ClienteDTO> buscarClientes(String filtro, Pageable pageable);
}

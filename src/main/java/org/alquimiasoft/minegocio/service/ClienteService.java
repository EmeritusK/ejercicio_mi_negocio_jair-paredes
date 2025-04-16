package org.alquimiasoft.minegocio.service;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO dto);
    Page<ClienteDTO> buscarClientes(String filtro, Pageable pageable);
}

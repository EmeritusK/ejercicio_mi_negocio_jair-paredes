package org.alquimiasoft.minegocio.mapper;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public Cliente toEntity(ClienteDTO dto) {
        return new Cliente(); 
    }

    public ClienteDTO toDto(Cliente cliente) {
        return new ClienteDTO(); 
    }
}

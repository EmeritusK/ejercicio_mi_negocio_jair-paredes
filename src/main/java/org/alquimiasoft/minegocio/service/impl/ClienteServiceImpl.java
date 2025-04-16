
package org.alquimiasoft.minegocio.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.exception.ConflictException;
import org.alquimiasoft.minegocio.exception.NotFoundException;
import org.alquimiasoft.minegocio.mapper.ClienteMapper;
import org.alquimiasoft.minegocio.repository.ClienteRepository;
import org.alquimiasoft.minegocio.service.ClienteService;

import lombok.RequiredArgsConstructor;

@Service        
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper mapper;

    @Override
    public ClienteDTO crearCliente(ClienteDTO dto) {
        if (clienteRepository.findByNumeroIdentificacion(dto.getNumeroIdentificacion()).isPresent()) {
            throw new ConflictException("Ya existe un cliente con esa identificación");
        }

        Cliente cliente = mapper.toEntity(dto);
        cliente.getDirecciones().forEach(d -> d.setCliente(cliente));
        return mapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    public ClienteDTO editarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        cliente.setNombres(dto.getNombres());
        cliente.setCorreo(dto.getCorreo());
        cliente.setCelular(dto.getCelular());
        return mapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }

    @Override
    public Page<ClienteDTO> buscarClientes(String filtro, Pageable pageable) {
        return clienteRepository.buscarPorFiltro(filtro, pageable).map(mapper::toDto);
    }
    
}

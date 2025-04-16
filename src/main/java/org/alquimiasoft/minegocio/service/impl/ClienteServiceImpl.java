package org.alquimiasoft.minegocio.service.impl;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.mapper.ClienteMapper;
import org.alquimiasoft.minegocio.repository.ClienteRepository;
import org.alquimiasoft.minegocio.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;


    @Override
    public ClienteDTO crearCliente(ClienteDTO dto) {
        if (clienteRepository.findByNumeroIdentificacion(dto.getNumeroIdentificacion()).isPresent()) {
            throw new RuntimeException("Cliente ya existe");
        }

        if (dto.getDirecciones() == null || dto.getDirecciones().isEmpty()) {
            throw new RuntimeException("El cliente debe tener al menos una direccion");
        }

        long direccionesMatriz = dto.getDirecciones() != null
                ? dto.getDirecciones().stream().filter(DireccionDTO::isMatriz).count()
                : 0;

        if (direccionesMatriz > 1) {
            throw new RuntimeException("Solo puede existir una direccion matriz");
        }

        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    public Page<ClienteDTO> buscarClientes(String filtro, Pageable pageable) {
        return clienteRepository.buscarPorFiltro(filtro, pageable)
                .map(clienteMapper::toDto);
    }
}

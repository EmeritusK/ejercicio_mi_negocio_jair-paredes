
package org.alquimiasoft.minegocio.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.exception.ClientValidationException;
import org.alquimiasoft.minegocio.exception.ConflictException;
import org.alquimiasoft.minegocio.exception.MissingFieldsException;
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
            throw new ConflictException("Ya existe un cliente con esa identificacion");
        }
        if (dto.getDirecciones() == null || dto.getDirecciones().isEmpty()) {
            throw new MissingFieldsException("El cliente debe tener al menos una direccion");
        }

        long direccionesMatriz = dto.getDirecciones().stream().filter(DireccionDTO::isMatriz).count();
        if (direccionesMatriz > 1) {
            throw new ClientValidationException("Solo puede existir una direccion matriz");
        }

        Cliente cliente = mapper.toEntity(dto);
        if (cliente.getDirecciones() != null) {
            cliente.getDirecciones().forEach(d -> d.setCliente(cliente));
        }
        return mapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    public ClienteDTO editarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        
        if (dto.getNumeroIdentificacion() != null && clienteRepository.findByNumeroIdentificacion(dto.getNumeroIdentificacion()).isPresent()) {
            throw new ConflictException("Ya existe un cliente con esa identificacion");
        }
        cliente.setNombres(dto.getNombres());
        cliente.setCorreo(dto.getCorreo());
        cliente.setCelular(dto.getCelular());
        cliente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
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
    if (filtro == null || filtro.trim().isEmpty()) {
        return clienteRepository.findAll(pageable).map(mapper::toDto);
    }

    String[] palabras = filtro.toLowerCase().split("\\s+");

    List<ClienteDTO> coincidencias = clienteRepository.findAll()
        .stream()
        .filter(cliente -> {
            String campo = (cliente.getNombres() + " " + cliente.getNumeroIdentificacion()).toLowerCase();
            return contieneTodasLasPalabras(campo, palabras);
        })
        .map(mapper::toDto)
        .toList();

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), coincidencias.size());

    List<ClienteDTO> pagina = coincidencias.subList(start, end);
    return new PageImpl<>(pagina, pageable, coincidencias.size());
}

private boolean contieneTodasLasPalabras(String texto, String[] palabras) {
    for (String palabra : palabras) {
        if (!texto.contains(palabra)) {
            return false;
        }
    }
    return true;
}


}

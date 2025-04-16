package org.alquimiasoft.minegocio.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.entity.Direccion;
import org.alquimiasoft.minegocio.exception.ConflictException;
import org.alquimiasoft.minegocio.exception.NotFoundException;
import org.alquimiasoft.minegocio.mapper.DireccionMapper;
import org.alquimiasoft.minegocio.repository.ClienteRepository;
import org.alquimiasoft.minegocio.repository.DireccionRepository;
import org.alquimiasoft.minegocio.service.DireccionService;

import lombok.RequiredArgsConstructor;

@Service        
@RequiredArgsConstructor
public class DireccionServiceImpl implements DireccionService {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;
    private final DireccionMapper direccionMapper;

    @Override
    public DireccionDTO registrarDireccion(Long clienteId, DireccionDTO dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (dto.isMatriz() && direccionRepository.countByClienteIdAndMatrizTrue(clienteId) > 0) {
            throw new ConflictException("El cliente ya tiene una dirección matriz.");
        }

        Direccion nueva = direccionMapper.toEntity(dto, cliente);
        return direccionMapper.toDto(direccionRepository.save(nueva));
    }

    @Override
    public List<DireccionDTO> obtenerDireccionesDeCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente no encontrado");
        }

        return direccionRepository.findByClienteId(clienteId).stream()
                .map(direccionMapper::toDto)
                .collect(Collectors.toList());
    }
}

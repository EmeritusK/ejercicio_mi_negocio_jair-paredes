package org.alquimiasoft.minegocio.service;

import java.util.List;

import org.alquimiasoft.minegocio.dto.DireccionDTO;

// ISP aplicado: Cada servicio tiene metodos propios del dominio que maneja.
public interface DireccionService {
    DireccionDTO registrarDireccion(Long clienteId, DireccionDTO dto);
    List<DireccionDTO> obtenerDireccionesDeCliente(Long clienteId);
}

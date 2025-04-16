package org.alquimiasoft.minegocio.service;

import java.util.List;

import org.alquimiasoft.minegocio.dto.DireccionDTO;

public interface DireccionService {
    DireccionDTO registrarDireccion(Long clienteId, DireccionDTO dto);
    List<DireccionDTO> obtenerDireccionesDeCliente(Long clienteId);
}

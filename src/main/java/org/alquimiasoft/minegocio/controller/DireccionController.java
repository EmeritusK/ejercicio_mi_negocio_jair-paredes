package org.alquimiasoft.minegocio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.service.DireccionService;

@RestController
@RequestMapping("/api/clientes/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @PostMapping("/{clienteId}")
    public ResponseEntity<DireccionDTO> registrar(@PathVariable Long clienteId, @RequestBody DireccionDTO dto) {
        return ResponseEntity.ok(direccionService.registrarDireccion(clienteId, dto));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<DireccionDTO>> listar(@PathVariable Long clienteId) {
        return ResponseEntity.ok(direccionService.obtenerDireccionesDeCliente(clienteId));
    }
}

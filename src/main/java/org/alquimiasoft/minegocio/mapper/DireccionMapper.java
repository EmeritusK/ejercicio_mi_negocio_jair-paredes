
package org.alquimiasoft.minegocio.mapper;

import org.springframework.stereotype.Component;

import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.entity.Direccion;

@Component
public class DireccionMapper {

    public DireccionDTO toDto(Direccion d) {
        return DireccionDTO.builder()
                .id(d.getId())
                .provincia(d.getProvincia())
                .ciudad(d.getCiudad())
                .direccion(d.getDireccion())
                .matriz(d.isMatriz())
                .build();
    }

    public Direccion toEntity(DireccionDTO dto, Cliente cliente) {
        return Direccion.builder()
                .id(dto.getId())
                .provincia(dto.getProvincia())
                .ciudad(dto.getCiudad())
                .direccion(dto.getDireccion())
                .matriz(dto.isMatriz())
                .cliente(cliente)
                .build();
    }
}

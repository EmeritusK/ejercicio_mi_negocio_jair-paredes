package org.alquimiasoft.minegocio.mapper;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.entity.Direccion;
import org.alquimiasoft.minegocio.enums.TipoIdentificacion;
import org.springframework.stereotype.Component;

import java.util.List;  
import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public ClienteDTO toDto(Cliente entity) {
        if (entity == null) return null;

        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setTipoIdentificacion(entity.getTipoIdentificacion().name());
        dto.setNumeroIdentificacion(entity.getNumeroIdentificacion());
        dto.setNombres(entity.getNombres());
        dto.setCorreo(entity.getCorreo());
        dto.setCelular(entity.getCelular());

        if (entity.getDirecciones() != null) {
            List<DireccionDTO> direcciones = entity.getDirecciones().stream()
                    .map(this::toDireccionDto)
                    .collect(Collectors.toList());
            dto.setDirecciones(direcciones);
        }

        return dto;
    }

    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;

        Cliente entity = new Cliente();
        entity.setId(dto.getId());
        entity.setTipoIdentificacion(TipoIdentificacion.valueOf(dto.getTipoIdentificacion()));
        entity.setNumeroIdentificacion(dto.getNumeroIdentificacion());
        entity.setNombres(dto.getNombres());
        entity.setCorreo(dto.getCorreo());
        entity.setCelular(dto.getCelular());

        if (dto.getDirecciones() != null) {
            List<Direccion> direcciones = dto.getDirecciones().stream()
                    .map(this::toDireccionEntity)
                    .collect(Collectors.toList());
            entity.setDirecciones(direcciones);
        }

        return entity;
    }

    private DireccionDTO toDireccionDto(Direccion d) {
        if (d == null) return null;

        DireccionDTO dto = new DireccionDTO();
        dto.setId(d.getId());
        dto.setProvincia(d.getProvincia());
        dto.setCiudad(d.getCiudad());
        dto.setDireccion(d.getDireccion());
        dto.setMatriz(d.isMatriz());

        return dto;
    }

    private Direccion toDireccionEntity(DireccionDTO dto) {
        if (dto == null) return null;

        Direccion d = new Direccion();
        d.setId(dto.getId());
        d.setProvincia(dto.getProvincia());
        d.setCiudad(dto.getCiudad());
        d.setDireccion(dto.getDireccion());
        d.setMatriz(dto.isMatriz());

        return d;
    }
}

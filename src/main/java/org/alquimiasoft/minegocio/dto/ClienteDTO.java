package org.alquimiasoft.minegocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.alquimiasoft.minegocio.enums.TipoIdentificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private String numeroIdentificacion;
    private TipoIdentificacion tipoIdentificacion;
    private String nombres;
    private String correo;
    private String celular;
    private List<DireccionDTO> direcciones;
}

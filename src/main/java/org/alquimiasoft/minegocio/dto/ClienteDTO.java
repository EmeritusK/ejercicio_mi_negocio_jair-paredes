
package org.alquimiasoft.minegocio.dto;

import java.util.List;

import org.alquimiasoft.minegocio.enums.TipoIdentificacion;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO {   
    private Long id;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String correo;
    private String celular;
    private List<DireccionDTO> direcciones;
}

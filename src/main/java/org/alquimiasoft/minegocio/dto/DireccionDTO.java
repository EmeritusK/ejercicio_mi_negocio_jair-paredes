
package org.alquimiasoft.minegocio.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DireccionDTO {
    private Long id;
    private String provincia;
    private String ciudad;
    private String direccion;
    private boolean matriz;
}

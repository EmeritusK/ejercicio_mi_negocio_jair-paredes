package org.alquimiasoft.minegocio.entity;

import org.alquimiasoft.minegocio.enums.TipoIdentificacion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    //solo los campos necesario para el TDD

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
}

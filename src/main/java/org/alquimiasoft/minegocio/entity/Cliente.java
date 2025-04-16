package org.alquimiasoft.minegocio.entity;

import java.util.List;

import org.alquimiasoft.minegocio.enums.TipoIdentificacion;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usamos un enum para el tipo de identificacion para que solo pueod ingresar una de las dos indicadas en el ejercicio
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion; 
    private String numeroIdentificacion;

    private String nombres;
    private String correo;
    private String celular;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones;
}
package com.facultad.sistemaavisos.estadoaviso;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "estados_aviso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoAviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_estado_aviso")
    private Long id;

    @Column(name = "codigo_interno_estado_aviso", nullable = false, unique = true, length = 50)
    private String codigoInterno;

    @Column(name = "fecha_alta_estado_aviso")
    private Instant fechaAltaEstadoAviso;

    @Column(name = "fecha_baja_estado_aviso")
    private Instant fechaBajaEstadoAviso;

    @Column(name = "nombre_estado_aviso", nullable = false, unique = true)
    private String nombreEstadoAviso;
}

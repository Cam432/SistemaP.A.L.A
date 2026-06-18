package com.facultad.sistemaavisos.estadopostulacion;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "estados_postulacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPostulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_estado_postulacion")
    private Long id;

    @Column(name = "codigo_interno_estado_postulacion", nullable = false, unique = true, length = 50)
    private String codigoInterno;

    @Column(name = "fecha_alta_estado_postulacion")
    private Instant fechaAltaEstadoPostulacion;

    @Column(name = "fecha_baja_estado_postulacion")
    private Instant fechaBajaEstadoPostulacion;

    @Column(name = "nombre_estado_postulacion", nullable = false, unique = true)
    private String nombreEstadoPostulacion;
}

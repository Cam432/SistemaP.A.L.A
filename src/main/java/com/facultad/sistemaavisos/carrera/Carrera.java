package com.facultad.sistemaavisos.carrera;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_carrera")
    private Long id;

    @Column(name = "descripcion_carrera")
    private String descripcionCarrera;

    @Column(name = "fecha_actualizacion_carrera")
    private Instant fechaActualizacionCarrera;

    @Column(name = "fecha_alta_carrera")
    private Instant fechaAltaCarrera;

    @Column(name = "fecha_baja_carrera")
    private Instant fechaBajaCarrera;

    @Column(name = "nombre_carrera", nullable = false)
    private String nombreCarrera;
}

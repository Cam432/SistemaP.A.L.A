package com.facultad.sistemaavisos.tipoestudiante;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "tipos_estudiante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_tipo_estudiante")
    private Long id;

    @Column(name = "fecha_alta_tipo_estudiante")
    private Instant fechaAltaTipoEstudiante;

    @Column(name = "fecha_baja_tipo_estudiante")
    private Instant fechaBajaTipoEstudiante;

}

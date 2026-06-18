package com.facultad.sistemaavisos.experienciaacademica;

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
@Table(name = "experiencias_academicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienciaAcademica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_exp_academica")
    private Long id;

    @Column(name = "fecha_baja_exp_academica")
    private Instant fechaBajaExpAcademica;

    @Column(name = "fecha_desde_exp_academica")
    private Instant fechaDesdeExpAcademica;

    @Column(name = "fecha_hasta_exp_academica")
    private Instant fechaHastaExpAcademica;

    @Column(name = "nombre_institucion_exp_academica", nullable = false)
    private String nombreInstitucionExpAcademica;

    @Column(name = "titulo_exp_academica", nullable = false)
    private String tituloExpAcademica;
}

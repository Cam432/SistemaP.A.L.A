package com.facultad.sistemaavisos.experiencia;

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
@Table(name = "experiencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experiencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_experiencia")
    private Long id;

    @Column(name = "descripcion_experiencia")
    private String descripcionExperiencia;

    @Column(name = "fecha_baja_experiencia")
    private Instant fechaBajaExperiencia;

    @Column(name = "fecha_desde_exp")
    private Instant fechaDesdeExp;

    @Column(name = "fecha_hasta_exp")
    private Instant fechaHastaExp;

    @Column(name = "nombre_cargo_experiencia", nullable = false)
    private String nombreCargoExperiencia;

    @Column(name = "nombre_empresa_experiencia", nullable = false)
    private String nombreEmpresaExperiencia;
}

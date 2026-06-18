package com.facultad.sistemaavisos.habilidad;

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
@Table(name = "habilidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_habilidad")
    private Long id;

    @Column(name = "fecha_baja_habilidad")
    private Instant fechaBajaHabilidad;

    @Column(name = "nombre_habilidad", nullable = false)
    private String nombreHabilidad;
}

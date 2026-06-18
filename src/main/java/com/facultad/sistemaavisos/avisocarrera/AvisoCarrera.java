package com.facultad.sistemaavisos.avisocarrera;

import com.facultad.sistemaavisos.aviso.Aviso;
import com.facultad.sistemaavisos.carrera.Carrera;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "avisos_carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoCarrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_aviso_carrera")
    private Long id;

    @Column(name = "prioridad_aviso_carrera")
    private Integer prioridadAvisoCarrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_aviso", nullable = false)
    private Aviso aviso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_carrera", nullable = false)
    private Carrera carrera;

    @Column(name = "fecha_asignacion_carrera")
    private Instant fechaAsignacionCarrera;

    @Column(name = "fecha_desasignacion_carrera")
    private Instant fechaDesasignacionCarrera;
}

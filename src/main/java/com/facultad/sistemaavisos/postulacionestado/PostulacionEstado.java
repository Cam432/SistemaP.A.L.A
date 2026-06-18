package com.facultad.sistemaavisos.postulacionestado;

import com.facultad.sistemaavisos.estadopostulacion.EstadoPostulacion;
import com.facultad.sistemaavisos.postulacion.Postulacion;
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
@Table(name = "postulaciones_estados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulacionEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_postulacion_estado")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_postulacion", nullable = false)
    private Postulacion postulacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_estado_postulacion", nullable = false)
    private EstadoPostulacion estadoPostulacion;

    @Column(name = "fecha_inicio_vigencia_ep")
    private Instant fechaInicioVigenciaEP;

    @Column(name = "fecha_fin_vigencia_ep")
    private Instant fechaFinVigenciaEP;
}

package com.facultad.sistemaavisos.solicitudestado;

import com.facultad.sistemaavisos.estadosolicitud.EstadoSolicitud;
import com.facultad.sistemaavisos.solicitudasociacion.SolicitudAsociacion;
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
@Table(name = "solicitudes_estados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_solicitud_estado")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_solicitud_asociacion", nullable = false)
    private SolicitudAsociacion solicitudAsociacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_estado_solicitud", nullable = false)
    private EstadoSolicitud estadoSolicitud;

    @Column(name = "fecha_inicio_vigencia_solicitud_estado")
    private Instant fechaInicioVigenciaSolicitudEstado;

    @Column(name = "fecha_fin_vigencia_solicitud")
    private Instant fechaFinVigenciaSolicitud;
}

package com.facultad.sistemaavisos.estadosolicitud;

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
@Table(name = "estados_solicitud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_estado_solicitud")
    private Long id;

    @Column(name = "codigo_interno_estado_solicitud", nullable = false, unique = true, length = 50)
    private String codigoInterno;

    @Column(name = "fecha_alta_estado_solicitud")
    private Instant fechaAltaEstadoSolicitud;

    @Column(name = "fecha_baja_estado_solicitud")
    private Instant fechaBajaEstadoSolicitud;

    @Column(name = "nombre_estado_solicitud", nullable = false, unique = true)
    private String nombreEstadoSolicitud;
}

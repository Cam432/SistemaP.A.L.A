package com.facultad.sistemaavisos.solicitudasociacion;

import com.facultad.sistemaavisos.administrador.Administrador;
import com.facultad.sistemaavisos.solicitudestado.SolicitudEstado;
import com.facultad.sistemaavisos.estadosolicitud.EstadoSolicitud;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitudes_asociacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudAsociacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_solicitud_asociacion")
    private Long id;

    @Column(name = "cuit_empresa_solicitud", nullable = false)
    private String cuitEmpresaSolicitud;

    @Column(name = "fecha_envio_solicitud")
    private Instant fechaEnvioSolicitud;

    @Column(name = "fecha_resolucion")
    private Instant fechaResolucion;

    @Column(name = "mail_empresa_solicitud", nullable = false)
    private String mailEmpresaSolicitud;

    @Column(name = "razon_social_empresa_solicitud", nullable = false)
    private String razonSocialEmpresaSolicitud;

    @Column(name = "telefono_empresa_solicitud")
    private String telefonoEmpresaSolicitud;

    @Column(name = "fecha_baja_solicitud_asociacion")
    private Instant fechaBajaSolicitudAsociacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_estado_solicitud", nullable = false)
    private EstadoSolicitud estadoActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_administrador")
    private Administrador administrador;

    @Builder.Default
    @OneToMany(mappedBy = "solicitudAsociacion", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<SolicitudEstado> solicitudEstados = new ArrayList<>();
}

package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.avisoestado.AvisoEstado;
import com.facultad.sistemaavisos.avisocarrera.AvisoCarrera;
import com.facultad.sistemaavisos.avisotipoaviso.AvisoTipoAviso;
import com.facultad.sistemaavisos.empresa.Empresa;
import com.facultad.sistemaavisos.estadoaviso.EstadoAviso;
import com.facultad.sistemaavisos.reclutador.Reclutador;

import jakarta.persistence.CascadeType;
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
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_aviso")
    private Long id;

    @Column(name = "descripcion_aviso")
    private String descripcionAviso;

    @Column(name = "fecha_baja_aviso")
    private Instant fechaBajaAviso;

    @Column(name = "fecha_cierre_aviso")
    private Instant fechaCierreAviso;

    @Column(name = "fecha_creacion_aviso")
    private Instant fechaCreacionAviso;

    @Column(name = "fecha_publicacion_aviso")
    private Instant fechaPublicacionAviso;

    @Column(name = "imagen_url_aviso")
    private String imagenUrlAviso;

    @Column(name = "nombre_aviso", nullable = false)
    private String nombreAviso;

    @ManyToOne
    @JoinColumn(name = "nro_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "nro_reclutador", nullable = false)
    private Reclutador reclutador;

    @ManyToOne
    @JoinColumn(name = "cod_estado_aviso", nullable = false)
    private EstadoAviso estadoActual;

    @Builder.Default
    @OneToMany(mappedBy = "aviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvisoEstado> avisosEstado = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "aviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvisoTipoAviso> avisosTipoAvisos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "aviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvisoCarrera> avisosCarrera = new ArrayList<>();
}

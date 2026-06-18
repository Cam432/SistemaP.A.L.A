package com.facultad.sistemaavisos.postulacion;

import com.facultad.sistemaavisos.aviso.Aviso;
import com.facultad.sistemaavisos.estadopostulacion.EstadoPostulacion;
import com.facultad.sistemaavisos.postulante.Postulante;
import com.facultad.sistemaavisos.postulacionestado.PostulacionEstado;

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
@Table(name = "postulaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_postulacion")
    private Long id;

    @Column(name = "descripcion_postulacion")
    private String descripcionPostulacion;

    @Column(name = "fecha_postulacion")
    private Instant fechaPostulacion;

    @Column(name = "url_cv_postulacion")
    private String urlCVPostulacion;

    @ManyToOne
    @JoinColumn(name = "legajo_academico_postulante", nullable = false)
    private Postulante postulante;

    @ManyToOne
    @JoinColumn(name = "nro_aviso", nullable = false)
    private Aviso aviso;

    @ManyToOne
    @JoinColumn(name = "cod_estado_postulacion", nullable = false)
    private EstadoPostulacion estadoActual;

    @Builder.Default
    @OneToMany(mappedBy = "postulacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostulacionEstado> postulacionEstados = new ArrayList<>();
}

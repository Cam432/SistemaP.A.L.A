package com.facultad.sistemaavisos.postulante;

import com.facultad.sistemaavisos.experiencia.Experiencia;
import com.facultad.sistemaavisos.experienciaacademica.ExperienciaAcademica;
import com.facultad.sistemaavisos.habilidad.Habilidad;
import com.facultad.sistemaavisos.postulantecarrera.PostulanteCarrera;
import com.facultad.sistemaavisos.tipoestudiante.TipoEstudiante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "postulantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_postulante", nullable = false)
    private Long id;

    @Column(name = "usuario_seguridad_id", unique = true)
    private Long usuarioSeguridadId;

    @Column(name = "nombre_postulante", nullable = false)
    private String nombrePostulante;

    @Column(name = "apellido_postulante", nullable = false)
    private String apellidoPostulante;

    @Column(name = "fecha_baja_postulante")
    private Instant fechaBajaPostulante;

    @Column(name = "fecha_nacimiento_postulante")
    private Instant fechaNacimientoPostulante;

    @Column(name = "legajo_academico_postulante", nullable = false, unique = true)
    private Long legajoAcademicoPostulante;

    @Column(name = "mail_academico_postulante", nullable = false, unique = true)
    private String mailAcademicoPostulante;

    @Column(name = "mail_personal_postulante")
    private String mailPersonalPostulante;

    @Column(name = "url_cv_guardado")
    private String urlCVGuardado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_tipo_estudiante", nullable = false)
    private TipoEstudiante tipoEstudiante;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nro_postulante")
    private List<Habilidad> habilidades = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nro_postulante")
    private List<ExperienciaAcademica> experienciasAcademicas = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nro_postulante")
    private List<Experiencia> experiencias = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "postulante", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostulanteCarrera> postulanteCarreras = new ArrayList<>();
}

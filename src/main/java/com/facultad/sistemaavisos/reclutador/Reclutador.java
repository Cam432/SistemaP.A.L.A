package com.facultad.sistemaavisos.reclutador;

import com.facultad.sistemaavisos.reclutadorempresa.ReclutadorEmpresa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "reclutadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclutador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_reclutador")
    private Long id;

    @Column(name = "usuario_seguridad_id", unique = true)
    private Long usuarioSeguridadId;

    @Column(name = "cuil_reclutador", nullable = false, unique = true, length = 20)
    private String cuilReclutador;

    @Column(name = "descripcion_reclutador")
    private String descripcionReclutador;

    @Column(name = "fecha_baja_reclutador")
    private Instant fechaBajaReclutador;

    @Column(name = "mail_reclutador", nullable = false, unique = true)
    private String mailReclutador;

    @Column(name = "nombre_reclutador", nullable = false)
    private String nombreReclutador;

    @Builder.Default
    @OneToMany(mappedBy = "reclutador", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<ReclutadorEmpresa> reclutadorEmpresas = new ArrayList<>();
}

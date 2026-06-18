package com.facultad.sistemaavisos.reclutadorempresa;

import com.facultad.sistemaavisos.empresa.Empresa;
import com.facultad.sistemaavisos.reclutador.Reclutador;
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
@Table(name = "reclutadores_empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclutadorEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_reclutador_empresa")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_reclutador", nullable = false)
    private Reclutador reclutador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "fecha_inicio_reclutador_empresa")
    private Instant fechaInicioReclutadorEmpresa;

    @Column(name = "fecha_fin_reclutador_empresa")
    private Instant fechaFinReclutadorEmpresa;
}

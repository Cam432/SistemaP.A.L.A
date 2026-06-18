package com.facultad.sistemaavisos.avisoestado;

import com.facultad.sistemaavisos.aviso.Aviso;
import com.facultad.sistemaavisos.estadoaviso.EstadoAviso;
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
@Table(name = "avisos_estados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_aviso_estado")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_aviso", nullable = false)
    private Aviso aviso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_estado_aviso", nullable = false)
    private EstadoAviso estadoAviso;

    @Column(name = "fecha_inicio_vigencia_estado")
    private Instant fechaInicioVigenciaEstado;

    @Column(name = "fecha_fin_vigencia_estado")
    private Instant fechaFinVigenciaEstado;
}

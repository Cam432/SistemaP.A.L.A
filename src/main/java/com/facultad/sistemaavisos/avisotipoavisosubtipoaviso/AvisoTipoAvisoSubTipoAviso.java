package com.facultad.sistemaavisos.avisotipoavisosubtipoaviso;

import com.facultad.sistemaavisos.avisotipoaviso.AvisoTipoAviso;
import com.facultad.sistemaavisos.subtipoaviso.SubTipoAviso;
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
@Table(name = "avisos_tipos_avisos_subtipos_avisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoTipoAvisoSubTipoAviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_aviso_tipo_aviso_sub_tipo_aviso")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contador_aviso_tipo_aviso", nullable = false)
    private AvisoTipoAviso avisoTipoAviso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_sub_tipo_aviso", nullable = false)
    private SubTipoAviso subTipoAviso;

    @Column(name = "fecha_hora_asignacion_aviso_tipo_aviso_sub_tipo_aviso")
    private Instant fechaHoraAsignacionAvisoTipoAvisoSubTipoAviso;

    @Column(name = "fecha_hora_desasignacion_aviso_tipo_aviso_sub_tipo_aviso")
    private Instant fechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso;
}

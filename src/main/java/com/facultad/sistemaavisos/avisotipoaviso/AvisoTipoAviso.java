package com.facultad.sistemaavisos.avisotipoaviso;

import com.facultad.sistemaavisos.aviso.Aviso;
import com.facultad.sistemaavisos.avisotipoavisosubtipoaviso.AvisoTipoAvisoSubTipoAviso;
import com.facultad.sistemaavisos.tipoaviso.TipoAviso;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avisos_tipos_avisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisoTipoAviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contador_aviso_tipo_aviso")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_aviso", nullable = false)
    private Aviso aviso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_tipo_aviso", nullable = false)
    private TipoAviso tipoAviso;

    @Column(name = "fecha_hora_asignacion_tipo_aviso")
    private Instant fechaHoraAsignacionTipoAviso;

    @Column(name = "fecha_hora_desasignacion_aviso_tipo_aviso")
    private Instant fechaHoraDesasignacionAvisoTipoAviso;

    @Builder.Default
    @OneToMany(mappedBy = "avisoTipoAviso", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvisoTipoAvisoSubTipoAviso> avisosTipoAvisosSubTiposAvisos = new ArrayList<>();
}

package com.facultad.sistemaavisos.tipoaviso;

import com.facultad.sistemaavisos.subtipoaviso.SubTipoAviso;
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
@Table(name = "tipos_aviso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_tipo_aviso")
    private Long id;

    @Column(name = "descripcion_tipo_aviso")
    private String descripcionTipoAviso;

    @Column(name = "fecha_baja_tipo_aviso")
    private Instant fechaBajaTipoAviso;

    @Column(name = "fecha_hora_alta_tipo_aviso")
    private Instant fechaHoraAltaTipoAviso;

    @Column(name = "nombre_tipo_aviso", nullable = false, unique = true)
    private String nombreTipoAviso;

    @Builder.Default
    @OneToMany(mappedBy = "tipoAviso", fetch = FetchType.LAZY)
    private List<SubTipoAviso> subTipoAvisos = new ArrayList<>();
}

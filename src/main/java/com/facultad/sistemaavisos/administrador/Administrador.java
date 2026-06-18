package com.facultad.sistemaavisos.administrador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_administrador")
    private Long id;

    @Column(name = "apellido_administrador", nullable = false)
    private String apellidoAdministrador;

    @Column(name = "fecha_alta_administrador")
    private Instant fechaAltaAdministrador;

    @Column(name = "fecha_baja_administrador")
    private Instant fechaBajaAdministrador;

    @Column(name = "legajo_administrador", nullable = false, unique = true)
    private Long legajoAdministrador;

    @Column(name = "mail_administrador", nullable = false, unique = true)
    private String mailAdministrador;

    @Column(name = "nombre_administrador", nullable = false)
    private String nombreAdministrador;
}

package com.facultad.sistemaavisos.estadoaviso;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoAvisoRepository extends JpaRepository<EstadoAviso, Long> {

    Optional<EstadoAviso> findByNombreEstadoAviso(String nombreEstadoAviso);

    Optional<EstadoAviso> findByCodigoInterno(String codigoInterno);
}

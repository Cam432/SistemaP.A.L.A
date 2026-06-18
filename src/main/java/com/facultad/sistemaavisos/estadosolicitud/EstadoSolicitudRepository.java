package com.facultad.sistemaavisos.estadosolicitud;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoSolicitudRepository extends JpaRepository<EstadoSolicitud, Long> {

    Optional<EstadoSolicitud> findByNombreEstadoSolicitud(String nombreEstadoSolicitud);

    Optional<EstadoSolicitud> findByCodigoInterno(String codigoInterno);
}

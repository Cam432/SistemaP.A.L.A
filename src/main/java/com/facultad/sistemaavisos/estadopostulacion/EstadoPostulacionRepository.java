package com.facultad.sistemaavisos.estadopostulacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoPostulacionRepository extends JpaRepository<EstadoPostulacion, Long> {

    Optional<EstadoPostulacion> findByNombreEstadoPostulacion(String nombreEstadoPostulacion);

    Optional<EstadoPostulacion> findByCodigoInterno(String codigoInterno);
}

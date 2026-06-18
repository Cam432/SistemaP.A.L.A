package com.facultad.sistemaavisos.shared.config;

import com.facultad.sistemaavisos.estadoaviso.EstadoAviso;
import com.facultad.sistemaavisos.estadoaviso.EstadoAvisoRepository;
import com.facultad.sistemaavisos.estadopostulacion.EstadoPostulacion;
import com.facultad.sistemaavisos.estadopostulacion.EstadoPostulacionRepository;
import com.facultad.sistemaavisos.estadosolicitud.EstadoSolicitud;
import com.facultad.sistemaavisos.estadosolicitud.EstadoSolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CatalogoEstadosInitializer {

    private final EstadoAvisoRepository estadoAvisoRepository;
    private final EstadoPostulacionRepository estadoPostulacionRepository;
    private final EstadoSolicitudRepository estadoSolicitudRepository;

    @Bean
    ApplicationRunner seedEstados() {
        return args -> {
            seedEstadosAviso();
            seedEstadosPostulacion();
            seedEstadosSolicitud();
        };
    }

    private void seedEstadosAviso() {
        final List<EstadoSeed> estados = List.of(
                new EstadoSeed("BORRADOR", "Borrador"),
                new EstadoSeed("ABIERTO", "Abierto"),
                new EstadoSeed("PAUSADO", "Pausado"),
                new EstadoSeed("CANCELADO", "Cancelado"),
                new EstadoSeed("CERRADO", "Cerrado")
        );

        estados.forEach(estado -> estadoAvisoRepository.findByCodigoInterno(estado.codigoInterno())
                .orElseGet(() -> estadoAvisoRepository.save(
                        EstadoAviso.builder()
                                .codigoInterno(estado.codigoInterno())
                                .nombreEstadoAviso(estado.nombreVisible())
                                .fechaAltaEstadoAviso(Instant.now())
                                .build()
                )));
    }

    private void seedEstadosPostulacion() {
        final List<EstadoSeed> estados = List.of(
                new EstadoSeed("ENVIADO", "Enviado"),
                new EstadoSeed("RECHAZADO", "Rechazado"),
                new EstadoSeed("CITADO", "Citado"),
                new EstadoSeed("ACEPTADO", "Aceptado"),
                new EstadoSeed("CANCELADO", "Cancelado")
        );

        estados.forEach(estado -> estadoPostulacionRepository.findByCodigoInterno(estado.codigoInterno())
                .orElseGet(() -> estadoPostulacionRepository.save(
                        EstadoPostulacion.builder()
                                .codigoInterno(estado.codigoInterno())
                                .nombreEstadoPostulacion(estado.nombreVisible())
                                .fechaAltaEstadoPostulacion(Instant.now())
                                .build()
                )));
    }

    private void seedEstadosSolicitud() {
        final List<EstadoSeed> estados = List.of(
                new EstadoSeed("ENVIADA", "Enviada"),
                new EstadoSeed("EN_EVALUACION", "En evaluacion"),
                new EstadoSeed("ACEPTADA", "Aceptada"),
                new EstadoSeed("RECHAZADA", "Rechazada"),
                new EstadoSeed("RESUELTA", "Resuelta")
        );

        estados.forEach(estado -> estadoSolicitudRepository.findByCodigoInterno(estado.codigoInterno())
                .orElseGet(() -> estadoSolicitudRepository.save(
                        EstadoSolicitud.builder()
                                .codigoInterno(estado.codigoInterno())
                                .nombreEstadoSolicitud(estado.nombreVisible())
                                .fechaAltaEstadoSolicitud(Instant.now())
                                .build()
                )));
    }

    private record EstadoSeed(String codigoInterno, String nombreVisible) {
    }
}

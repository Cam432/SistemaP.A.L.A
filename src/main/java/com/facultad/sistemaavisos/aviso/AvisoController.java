package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoDetalleResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoResumenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/avisos")
@RequiredArgsConstructor
public class AvisoController {

    private final AvisoService avisoService;

    @GetMapping("/disponibles")
    @PreAuthorize("hasAuthority('VER_AVISOS')")
    public List<AvisoResumenResponse> listarDisponibles() {
        return avisoService.listarDisponibles();
    }

    @GetMapping("/disponibles/{avisoId}")
    @PreAuthorize("hasAuthority('VER_AVISOS')")
    public AvisoDetalleResponse obtenerDetalle(@PathVariable Long avisoId) {
        return avisoService.obtenerDetalleDisponible(avisoId);
    }
}

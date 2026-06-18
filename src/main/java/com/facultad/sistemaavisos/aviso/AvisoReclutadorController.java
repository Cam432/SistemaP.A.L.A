package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoResponse;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoUpdateRequest;

@RestController
@RequestMapping("/api/reclutadores/{reclutadorId}/avisos")
@RequiredArgsConstructor
public class AvisoReclutadorController {

    private final AvisoService avisoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoResponse crearAviso(
            @PathVariable Long reclutadorId,
            @RequestBody @Valid AvisoCreateRequest request
    ) {
        return avisoService.crearAviso(reclutadorId, request);
    }

    @GetMapping("/{avisoId}")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoResponse obtenerAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.obtenerAvisoDelReclutador(reclutadorId, avisoId);
    }

    @PutMapping("/{avisoId}")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoResponse actualizarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId,
            @RequestBody @Valid AvisoUpdateRequest request
    ) {
        return avisoService.actualizarAviso(reclutadorId, avisoId, request);
    }

    @PatchMapping("/{avisoId}/pausar")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoResponse pausarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.pausarAviso(reclutadorId, avisoId);
    }

    @PatchMapping("/{avisoId}/reanudar")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoResponse reanudarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.reanudarAviso(reclutadorId, avisoId);
    }

    @PatchMapping("/{avisoId}/cancelar")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoResponse cancelarAviso(
            @PathVariable Long reclutadorId,
            @PathVariable Long avisoId
    ) {
        return avisoService.cancelarAviso(reclutadorId, avisoId);
    }
}

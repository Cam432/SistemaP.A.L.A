package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoFormularioSoporteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvisoSoporteController {

    private final AvisoSoporteService avisoSoporteService;

    @GetMapping("/reclutadores/{reclutadorId}/avisos/soporte")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public AvisoFormularioSoporteResponse obtenerSoporteFormulario(@PathVariable Long reclutadorId) {
        return avisoSoporteService.obtenerSoporteFormulario(reclutadorId);
    }

    @GetMapping("/reclutadores/{reclutadorId}/avisos/soporte/empresas-activas")
    @PreAuthorize("hasRole('Reclutador') and @authorizationService.esReclutador(#reclutadorId)")
    public List<AvisoFormularioSoporteResponse.EmpresaActivaResponse> listarEmpresasActivas(
            @PathVariable Long reclutadorId
    ) {
        return avisoSoporteService.listarEmpresasActivasDelReclutador(reclutadorId);
    }

    @GetMapping("/avisos/soporte/carreras-activas")
    @PreAuthorize("hasAnyRole('Reclutador','Administrador')")
    public List<AvisoFormularioSoporteResponse.CarreraActivaResponse> listarCarrerasActivas() {
        return avisoSoporteService.listarCarrerasActivas();
    }

    @GetMapping("/avisos/soporte/tipos-aviso-activos")
    @PreAuthorize("hasAnyRole('Reclutador','Administrador')")
    public List<AvisoFormularioSoporteResponse.TipoAvisoActivoResponse> listarTiposAvisoActivos() {
        return avisoSoporteService.listarTiposAvisoActivos();
    }
}

package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoDetalleResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoResumenResponse;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoCreateRequest;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoUpdateRequest;

import java.util.List;

public interface AvisoService {

    List<Aviso> listarTodos();

    Aviso buscarPorId(Long avisoId);

    Aviso guardar(Aviso aviso);

    AvisoResponse crearAviso(Long reclutadorId, AvisoCreateRequest request);

    AvisoResponse obtenerAvisoDelReclutador(Long reclutadorId, Long avisoId);

    AvisoResponse actualizarAviso(Long reclutadorId, Long avisoId, AvisoUpdateRequest request);

    AvisoResponse pausarAviso(Long reclutadorId, Long avisoId);

    AvisoResponse reanudarAviso(Long reclutadorId, Long avisoId);

    AvisoResponse cancelarAviso(Long reclutadorId, Long avisoId);

    void eliminar(Long avisoId);

    List<AvisoResumenResponse> listarDisponibles();

    AvisoDetalleResponse obtenerDetalleDisponible(Long avisoId);
}

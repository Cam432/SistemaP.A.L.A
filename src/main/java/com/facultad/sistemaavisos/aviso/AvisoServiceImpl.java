package com.facultad.sistemaavisos.aviso;

import com.facultad.sistemaavisos.aviso.dto.AvisoDetalleResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoResumenResponse;
import com.facultad.sistemaavisos.aviso.dto.AvisoTipoResponse;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoCreateRequest;
import com.facultad.sistemaavisos.aviso.dto.request.AvisoUpdateRequest;
import com.facultad.sistemaavisos.avisoestado.AvisoEstado;
import com.facultad.sistemaavisos.avisocarrera.AvisoCarrera;
import com.facultad.sistemaavisos.avisotipoaviso.AvisoTipoAviso;
import com.facultad.sistemaavisos.avisotipoavisosubtipoaviso.AvisoTipoAvisoSubTipoAviso;
import com.facultad.sistemaavisos.carrera.Carrera;
import com.facultad.sistemaavisos.carrera.CarreraRepository;
import com.facultad.sistemaavisos.empresa.Empresa;
import com.facultad.sistemaavisos.empresa.EmpresaRepository;
import com.facultad.sistemaavisos.estadoaviso.EstadoAviso;
import com.facultad.sistemaavisos.estadoaviso.EstadoAvisoRepository;
import com.facultad.sistemaavisos.reclutador.Reclutador;
import com.facultad.sistemaavisos.reclutador.ReclutadorRepository;
import com.facultad.sistemaavisos.reclutadorempresa.ReclutadorEmpresaRepository;
import com.facultad.sistemaavisos.shared.exception.OperacionInvalidaException;
import com.facultad.sistemaavisos.shared.exception.RecursoNoEncontradoException;
import com.facultad.sistemaavisos.subtipoaviso.SubTipoAviso;
import com.facultad.sistemaavisos.subtipoaviso.SubTipoAvisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.facultad.sistemaavisos.tipoaviso.TipoAviso;
import com.facultad.sistemaavisos.tipoaviso.TipoAvisoRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvisoServiceImpl implements AvisoService {

    private static final String CODIGO_ESTADO_DISPONIBLE = "ABIERTO";
    private static final String CODIGO_ESTADO_BORRADOR = "BORRADOR";
    private static final String CODIGO_ESTADO_PAUSADO = "PAUSADO";
    private static final String CODIGO_ESTADO_CANCELADO = "CANCELADO";

    private final AvisoRepository avisoRepository;
    private final EmpresaRepository empresaRepository;
    private final ReclutadorRepository reclutadorRepository;
    private final EstadoAvisoRepository estadoAvisoRepository;
    private final CarreraRepository carreraRepository;
    private final TipoAvisoRepository tipoAvisoRepository;
    private final SubTipoAvisoRepository subTipoAvisoRepository;
    private final ReclutadorEmpresaRepository reclutadorEmpresaRepository;

    @Override
    public List<Aviso> listarTodos() {
        return avisoRepository.findAll();
    }

    @Override
    public Aviso buscarPorId(Long avisoId) {
        return avisoRepository.findByIdAndFechaBajaAvisoIsNull(avisoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el aviso con id: " + avisoId
                ));
    }

    @Override
    @Transactional
    public Aviso guardar(Aviso aviso) {
        return avisoRepository.save(aviso);
    }

    @Override
    @Transactional
    public AvisoResponse crearAviso(Long reclutadorId, AvisoCreateRequest request) {
        final Reclutador reclutador = buscarReclutadorActivoPorId(reclutadorId);
        final Empresa empresa = buscarEmpresaActivaDelReclutador(reclutadorId, request.empresaId());
        validarCarrerasSinDuplicados(request.carreras());
        validarTiposSinDuplicados(request.tiposAviso());

        final Instant ahora = Instant.now();
        final EstadoAviso estadoInicial = buscarEstadoInicial(request.guardarComoBorrador());

        final Aviso aviso = Aviso.builder()
                .nombreAviso(request.nombreAviso())
                .descripcionAviso(request.descripcionAviso())
                .fechaCreacionAviso(ahora)
                .fechaPublicacionAviso(request.guardarComoBorrador()
                        ? request.fechaPublicacionAviso()
                        : request.fechaPublicacionAviso() != null ? request.fechaPublicacionAviso() : ahora)
                .fechaCierreAviso(request.fechaCierreAviso())
                .imagenUrlAviso(request.imagenUrlAviso())
                .empresa(empresa)
                .reclutador(reclutador)
                .estadoActual(estadoInicial)
                .build();

        aviso.getAvisosEstado().add(AvisoEstado.builder()
                .aviso(aviso)
                .estadoAviso(estadoInicial)
                .fechaInicioVigenciaEstado(ahora)
                .build());

        for (AvisoCreateRequest.CarreraAvisoRequest carreraRequest : request.carreras()) {
            final Carrera carrera = buscarCarreraActivaPorId(carreraRequest.carreraId());
            aviso.getAvisosCarrera().add(AvisoCarrera.builder()
                    .aviso(aviso)
                    .carrera(carrera)
                    .prioridadAvisoCarrera(carreraRequest.prioridad())
                    .fechaAsignacionCarrera(ahora)
                    .build());
        }

        for (AvisoCreateRequest.TipoAvisoSeleccionRequest tipoRequest : request.tiposAviso()) {
            final TipoAviso tipoAviso = buscarTipoAvisoActivoPorId(tipoRequest.tipoAvisoId());
            validarSubTiposSinDuplicados(tipoRequest);

            final AvisoTipoAviso avisoTipoAviso = AvisoTipoAviso.builder()
                    .aviso(aviso)
                    .tipoAviso(tipoAviso)
                    .fechaHoraAsignacionTipoAviso(ahora)
                    .build();

            final Map<Long, SubTipoAviso> subTiposActivos = tipoAviso.getSubTipoAvisos().stream()
                    .filter(subTipoAviso -> subTipoAviso.getFechaBajaSubTipoAviso() == null)
                    .collect(Collectors.toMap(SubTipoAviso::getId, Function.identity()));

            for (Long subTipoAvisoId : tipoRequest.subTipoAvisoIds()) {
                final SubTipoAviso subTipoAviso = buscarSubTipoValidoParaTipo(subTipoAvisoId, subTiposActivos, tipoAviso);
                avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().add(AvisoTipoAvisoSubTipoAviso.builder()
                        .avisoTipoAviso(avisoTipoAviso)
                        .subTipoAviso(subTipoAviso)
                        .fechaHoraAsignacionAvisoTipoAvisoSubTipoAviso(ahora)
                        .build());
            }

            aviso.getAvisosTipoAvisos().add(avisoTipoAviso);
        }

        return toAvisoResponse(avisoRepository.save(aviso));
    }

    @Override
    public AvisoResponse obtenerAvisoDelReclutador(Long reclutadorId, Long avisoId) {
        final Aviso aviso = buscarPorId(avisoId);
        buscarReclutadorActivoPorId(reclutadorId);
        validarReclutadorPropietarioDelAviso(aviso, reclutadorId);
        return toAvisoResponse(aviso);
    }

    @Override
    @Transactional
    public AvisoResponse actualizarAviso(Long reclutadorId, Long avisoId, AvisoUpdateRequest request) {
        final Aviso existente = buscarPorId(avisoId);
        final Reclutador reclutador = buscarReclutadorActivoPorId(reclutadorId);
        validarReclutadorPropietarioDelAviso(existente, reclutadorId);
        final Empresa empresa = buscarEmpresaActivaDelReclutador(reclutadorId, request.empresaId());
        validarCarrerasSinDuplicados(request.carreras());
        validarTiposSinDuplicados(request.tiposAviso());

        final Instant ahora = Instant.now();
        final EstadoAviso estadoObjetivo = resolverEstadoEdicion(existente, request.guardarComoBorrador());
        final Long estadoAnteriorId = existente.getEstadoActual() != null ? existente.getEstadoActual().getId() : null;

        existente.actualizarDatos(
                request,
                empresa,
                reclutador,
                estadoObjetivo
        );

        if (!Boolean.TRUE.equals(request.guardarComoBorrador())) {
            final Instant fechaPublicacion = request.fechaPublicacionAviso() != null
                    ? request.fechaPublicacionAviso()
                    : existente.getFechaPublicacionAviso() != null ? existente.getFechaPublicacionAviso() : ahora;
            existente.setFechaPublicacionAviso(fechaPublicacion);
        }

        if (!Objects.equals(
                estadoAnteriorId,
                estadoObjetivo.getId()
        )) {
            cerrarEstadoActivo(existente, ahora);
            existente.setEstadoActual(estadoObjetivo);
            existente.getAvisosEstado().add(AvisoEstado.builder()
                    .aviso(existente)
                    .estadoAviso(estadoObjetivo)
                    .fechaInicioVigenciaEstado(ahora)
                    .build());
        }

        sincronizarCarreras(existente, request.carreras(), ahora);
        sincronizarTiposAviso(existente, request.tiposAviso(), ahora);

        return toAvisoResponse(avisoRepository.save(existente));
    }

    @Override
    @Transactional
    public AvisoResponse pausarAviso(Long reclutadorId, Long avisoId) {
        return controlarEstadoAviso(
                reclutadorId,
                avisoId,
                CODIGO_ESTADO_DISPONIBLE,
                CODIGO_ESTADO_PAUSADO
        );
    }

    @Override
    @Transactional
    public AvisoResponse reanudarAviso(Long reclutadorId, Long avisoId) {
        return controlarEstadoAviso(
                reclutadorId,
                avisoId,
                CODIGO_ESTADO_PAUSADO,
                CODIGO_ESTADO_DISPONIBLE
        );
    }

    @Override
    @Transactional
    public AvisoResponse cancelarAviso(Long reclutadorId, Long avisoId) {
        final Aviso aviso = buscarPorId(avisoId);
        buscarReclutadorActivoPorId(reclutadorId);
        validarReclutadorPropietarioDelAviso(aviso, reclutadorId);

        final String estadoActual = aviso.getEstadoActual() != null
                ? aviso.getEstadoActual().getCodigoInterno()
                : null;

        if (!CODIGO_ESTADO_DISPONIBLE.equals(estadoActual) && !CODIGO_ESTADO_PAUSADO.equals(estadoActual)) {
            throw new OperacionInvalidaException("La opcion no esta disponible");
        }

        final EstadoAviso estadoCancelado = buscarEstadoActivoPorCodigo(CODIGO_ESTADO_CANCELADO);
        final Instant ahora = Instant.now();

        cerrarEstadoActivo(aviso, ahora);
        aviso.setEstadoActual(estadoCancelado);
        aviso.getAvisosEstado().add(AvisoEstado.builder()
                .aviso(aviso)
                .estadoAviso(estadoCancelado)
                .fechaInicioVigenciaEstado(ahora)
                .build());

        return toAvisoResponse(avisoRepository.save(aviso));
    }

    @Override
    @Transactional
    public void eliminar(Long avisoId) {
        final Aviso aviso = buscarPorId(avisoId);
        aviso.darDeBaja();
        avisoRepository.save(aviso);
    }

    @Override
    public List<AvisoResumenResponse> listarDisponibles() {
        return avisoRepository
                .findDistinctByEstadoActual_CodigoInternoAndFechaBajaAvisoIsNullAndEstadoActual_FechaBajaEstadoAvisoIsNull(
                        CODIGO_ESTADO_DISPONIBLE
                )
                .stream()
                .map(this::toResumenResponse)
                .toList();
    }

    @Override
    public AvisoDetalleResponse obtenerDetalleDisponible(Long avisoId) {
        final Aviso aviso = avisoRepository
                .findByIdAndEstadoActual_CodigoInternoAndFechaBajaAvisoIsNullAndEstadoActual_FechaBajaEstadoAvisoIsNull(
                        avisoId,
                        CODIGO_ESTADO_DISPONIBLE
                )
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro un aviso disponible con id: " + avisoId
                ));
        return toDetalleResponse(aviso);
    }

    private Reclutador buscarReclutadorActivoPorId(Long reclutadorId) {
        return reclutadorRepository.findByIdAndFechaBajaReclutadorIsNull(reclutadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el reclutador activo con id: " + reclutadorId
                ));
    }

    private Empresa buscarEmpresaActivaDelReclutador(Long reclutadorId, Long empresaId) {
        reclutadorEmpresaRepository.findByReclutador_IdAndEmpresa_IdAndFechaFinReclutadorEmpresaIsNull(
                        reclutadorId,
                        empresaId
                )
                .orElseThrow(() -> new OperacionInvalidaException(
                        "La empresa seleccionada no se encuentra activa para el reclutador indicado"
                ));

        final Empresa empresa = buscarEmpresaPorId(empresaId);
        if (empresa.getFechaBajaEmpresa() != null) {
            throw new OperacionInvalidaException(
                    "La empresa seleccionada se encuentra dada de baja"
            );
        }

        return empresa;
    }

    private Empresa buscarEmpresaPorId(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la empresa con id: " + empresaId
                ));
    }

    private Reclutador buscarReclutadorPorId(Long reclutadorId) {
        return reclutadorRepository.findById(reclutadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el reclutador con id: " + reclutadorId
                ));
    }

    private void validarReclutadorPropietarioDelAviso(Aviso aviso, Long reclutadorId) {
        if (aviso.getReclutador() == null || !Objects.equals(aviso.getReclutador().getId(), reclutadorId)) {
            throw new OperacionInvalidaException(
                    "El aviso no pertenece al reclutador indicado"
            );
        }
    }

    private AvisoResponse controlarEstadoAviso(
            Long reclutadorId,
            Long avisoId,
            String codigoEsperado,
            String codigoObjetivo
    ) {
        final Aviso aviso = buscarPorId(avisoId);
        buscarReclutadorActivoPorId(reclutadorId);
        validarReclutadorPropietarioDelAviso(aviso, reclutadorId);

        final String estadoActual = aviso.getEstadoActual() != null
                ? aviso.getEstadoActual().getCodigoInterno()
                : null;

        if (!Objects.equals(estadoActual, codigoEsperado)) {
            throw new OperacionInvalidaException("La opcion no esta disponible");
        }

        final EstadoAviso nuevoEstado = buscarEstadoActivoPorCodigo(codigoObjetivo);
        final Instant ahora = Instant.now();

        cerrarEstadoActivo(aviso, ahora);
        aviso.setEstadoActual(nuevoEstado);
        aviso.getAvisosEstado().add(AvisoEstado.builder()
                .aviso(aviso)
                .estadoAviso(nuevoEstado)
                .fechaInicioVigenciaEstado(ahora)
                .build());

        return toAvisoResponse(avisoRepository.save(aviso));
    }

    private Carrera buscarCarreraActivaPorId(Long carreraId) {
        return carreraRepository.findByIdAndFechaBajaCarreraIsNull(carreraId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la carrera activa con id: " + carreraId
                ));
    }

    private TipoAviso buscarTipoAvisoActivoPorId(Long tipoAvisoId) {
        return tipoAvisoRepository.findByIdAndFechaBajaTipoAvisoIsNull(tipoAvisoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el tipo de aviso activo con id: " + tipoAvisoId
                ));
    }

    private SubTipoAviso buscarSubTipoValidoParaTipo(
            Long subTipoAvisoId,
            Map<Long, SubTipoAviso> subTiposActivos,
            TipoAviso tipoAviso
    ) {
        final SubTipoAviso subTipoAviso = subTiposActivos.get(subTipoAvisoId);
        if (subTipoAviso != null) {
            return subTipoAviso;
        }

        final SubTipoAviso subtipoExistente = subTipoAvisoRepository.findById(subTipoAvisoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el subtipo de aviso con id: " + subTipoAvisoId
                ));

        if (subtipoExistente.getFechaBajaSubTipoAviso() != null) {
            throw new OperacionInvalidaException(
                    "El subtipo de aviso con id " + subTipoAvisoId + " se encuentra dado de baja"
            );
        }

        throw new OperacionInvalidaException(
                "El subtipo de aviso con id " + subTipoAvisoId +
                        " no pertenece al tipo de aviso " + tipoAviso.getNombreTipoAviso()
        );
    }

    private EstadoAviso buscarEstadoAvisoPorId(Long estadoAvisoId) {
        return estadoAvisoRepository.findById(estadoAvisoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el estado de aviso con id: " + estadoAvisoId
                ));
    }

    private EstadoAviso resolverEstadoEdicion(Aviso aviso, Boolean guardarComoBorrador) {
        final String estadoActual = aviso.getEstadoActual() != null ? aviso.getEstadoActual().getCodigoInterno() : null;

        if (Boolean.TRUE.equals(guardarComoBorrador)) {
            if (CODIGO_ESTADO_DISPONIBLE.equals(estadoActual)) {
                throw new OperacionInvalidaException(
                        "Un aviso abierto no puede volver a borrador"
                );
            }

            return buscarEstadoInicial(true);
        }

        return buscarEstadoInicial(false);
    }

    private void cerrarEstadoActivo(Aviso aviso, Instant ahora) {
        aviso.getAvisosEstado().stream()
                .filter(avisoEstado -> avisoEstado.getFechaFinVigenciaEstado() == null)
                .forEach(avisoEstado -> avisoEstado.setFechaFinVigenciaEstado(ahora));
    }

    private EstadoAviso buscarEstadoInicial(Boolean guardarComoBorrador) {
        final String codigoInterno = Boolean.TRUE.equals(guardarComoBorrador)
                ? CODIGO_ESTADO_BORRADOR
                : CODIGO_ESTADO_DISPONIBLE;

        return buscarEstadoActivoPorCodigo(codigoInterno);
    }

    private EstadoAviso buscarEstadoActivoPorCodigo(String codigoInterno) {
        final EstadoAviso estadoAviso = estadoAvisoRepository.findByCodigoInterno(codigoInterno)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el estado de aviso con codigo interno: " + codigoInterno
                ));

        if (estadoAviso.getFechaBajaEstadoAviso() != null) {
            throw new OperacionInvalidaException(
                    "El estado de aviso " + codigoInterno + " se encuentra dado de baja"
            );
        }

        return estadoAviso;
    }

    private void validarCarrerasSinDuplicados(List<AvisoCreateRequest.CarreraAvisoRequest> carreras) {
        final Set<Long> carrerasIds = new HashSet<>();
        final Set<Integer> prioridades = new HashSet<>();

        for (AvisoCreateRequest.CarreraAvisoRequest carrera : carreras) {
            if (!carrerasIds.add(carrera.carreraId())) {
                throw new OperacionInvalidaException(
                        "No se puede seleccionar la misma carrera mas de una vez"
                );
            }

            if (!prioridades.add(carrera.prioridad())) {
                throw new OperacionInvalidaException(
                        "No se puede repetir la prioridad entre carreras del aviso"
                );
            }
        }
    }

    private void validarTiposSinDuplicados(List<AvisoCreateRequest.TipoAvisoSeleccionRequest> tiposAviso) {
        final Set<Long> tiposIds = new HashSet<>();
        for (AvisoCreateRequest.TipoAvisoSeleccionRequest tipoAviso : tiposAviso) {
            if (!tiposIds.add(tipoAviso.tipoAvisoId())) {
                throw new OperacionInvalidaException(
                        "No se puede seleccionar el mismo tipo de aviso mas de una vez"
                );
            }
        }
    }

    private void validarSubTiposSinDuplicados(AvisoCreateRequest.TipoAvisoSeleccionRequest tipoAviso) {
        final Set<Long> subTiposIds = new HashSet<>(tipoAviso.subTipoAvisoIds());
        if (subTiposIds.size() != tipoAviso.subTipoAvisoIds().size()) {
            throw new OperacionInvalidaException(
                    "No se puede repetir el mismo subtipo dentro de un tipo de aviso"
            );
        }
    }

    private void sincronizarCarreras(
            Aviso aviso,
            List<AvisoCreateRequest.CarreraAvisoRequest> carrerasRequest,
            Instant ahora
    ) {
        final Map<Long, AvisoCarrera> carrerasActivas = aviso.getAvisosCarrera().stream()
                .filter(avisoCarrera -> avisoCarrera.getFechaDesasignacionCarrera() == null)
                .collect(Collectors.toMap(avisoCarrera -> avisoCarrera.getCarrera().getId(), Function.identity()));

        final Set<Long> carrerasSolicitadas = carrerasRequest.stream()
                .map(AvisoCreateRequest.CarreraAvisoRequest::carreraId)
                .collect(Collectors.toSet());

        carrerasActivas.values().stream()
                .filter(avisoCarrera -> !carrerasSolicitadas.contains(avisoCarrera.getCarrera().getId()))
                .forEach(avisoCarrera -> avisoCarrera.setFechaDesasignacionCarrera(ahora));

        for (AvisoCreateRequest.CarreraAvisoRequest carreraRequest : carrerasRequest) {
            final Carrera carrera = buscarCarreraActivaPorId(carreraRequest.carreraId());
            final AvisoCarrera carreraActiva = carrerasActivas.get(carreraRequest.carreraId());

            if (carreraActiva == null) {
                aviso.getAvisosCarrera().add(AvisoCarrera.builder()
                        .aviso(aviso)
                        .carrera(carrera)
                        .prioridadAvisoCarrera(carreraRequest.prioridad())
                        .fechaAsignacionCarrera(ahora)
                        .build());
                continue;
            }

            if (!Objects.equals(carreraActiva.getPrioridadAvisoCarrera(), carreraRequest.prioridad())) {
                carreraActiva.setFechaDesasignacionCarrera(ahora);
                aviso.getAvisosCarrera().add(AvisoCarrera.builder()
                        .aviso(aviso)
                        .carrera(carrera)
                        .prioridadAvisoCarrera(carreraRequest.prioridad())
                        .fechaAsignacionCarrera(ahora)
                        .build());
            }
        }
    }

    private void sincronizarTiposAviso(
            Aviso aviso,
            List<AvisoCreateRequest.TipoAvisoSeleccionRequest> tiposRequest,
            Instant ahora
    ) {
        final Map<Long, AvisoTipoAviso> tiposActivos = aviso.getAvisosTipoAvisos().stream()
                .filter(avisoTipoAviso -> avisoTipoAviso.getFechaHoraDesasignacionAvisoTipoAviso() == null)
                .collect(Collectors.toMap(avisoTipoAviso -> avisoTipoAviso.getTipoAviso().getId(), Function.identity()));

        final Set<Long> tiposSolicitados = tiposRequest.stream()
                .map(AvisoCreateRequest.TipoAvisoSeleccionRequest::tipoAvisoId)
                .collect(Collectors.toSet());

        tiposActivos.values().stream()
                .filter(avisoTipoAviso -> !tiposSolicitados.contains(avisoTipoAviso.getTipoAviso().getId()))
                .forEach(avisoTipoAviso -> desasignarTipoCompleto(avisoTipoAviso, ahora));

        for (AvisoCreateRequest.TipoAvisoSeleccionRequest tipoRequest : tiposRequest) {
            validarSubTiposSinDuplicados(tipoRequest);
            final TipoAviso tipoAviso = buscarTipoAvisoActivoPorId(tipoRequest.tipoAvisoId());
            final Map<Long, SubTipoAviso> subTiposActivosCatalogo = tipoAviso.getSubTipoAvisos().stream()
                    .filter(subTipoAviso -> subTipoAviso.getFechaBajaSubTipoAviso() == null)
                    .collect(Collectors.toMap(SubTipoAviso::getId, Function.identity()));

            final AvisoTipoAviso tipoActivo = tiposActivos.get(tipoRequest.tipoAvisoId());
            if (tipoActivo == null) {
                crearTipoCompleto(aviso, tipoAviso, tipoRequest, subTiposActivosCatalogo, ahora);
                continue;
            }

            sincronizarSubTipos(tipoActivo, tipoRequest, subTiposActivosCatalogo, tipoAviso, ahora);
        }
    }

    private void crearTipoCompleto(
            Aviso aviso,
            TipoAviso tipoAviso,
            AvisoCreateRequest.TipoAvisoSeleccionRequest tipoRequest,
            Map<Long, SubTipoAviso> subTiposActivosCatalogo,
            Instant ahora
    ) {
        final AvisoTipoAviso nuevoTipo = AvisoTipoAviso.builder()
                .aviso(aviso)
                .tipoAviso(tipoAviso)
                .fechaHoraAsignacionTipoAviso(ahora)
                .build();

        for (Long subTipoAvisoId : tipoRequest.subTipoAvisoIds()) {
            final SubTipoAviso subTipoAviso = buscarSubTipoValidoParaTipo(subTipoAvisoId, subTiposActivosCatalogo, tipoAviso);
            nuevoTipo.getAvisosTipoAvisosSubTiposAvisos().add(AvisoTipoAvisoSubTipoAviso.builder()
                    .avisoTipoAviso(nuevoTipo)
                    .subTipoAviso(subTipoAviso)
                    .fechaHoraAsignacionAvisoTipoAvisoSubTipoAviso(ahora)
                    .build());
        }

        aviso.getAvisosTipoAvisos().add(nuevoTipo);
    }

    private void sincronizarSubTipos(
            AvisoTipoAviso avisoTipoAviso,
            AvisoCreateRequest.TipoAvisoSeleccionRequest tipoRequest,
            Map<Long, SubTipoAviso> subTiposActivosCatalogo,
            TipoAviso tipoAviso,
            Instant ahora
    ) {
        final Map<Long, AvisoTipoAvisoSubTipoAviso> subTiposActivos = avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().stream()
                .filter(subTipo -> subTipo.getFechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso() == null)
                .collect(Collectors.toMap(subTipo -> subTipo.getSubTipoAviso().getId(), Function.identity()));

        final Set<Long> subTiposSolicitados = new HashSet<>(tipoRequest.subTipoAvisoIds());

        subTiposActivos.values().stream()
                .filter(subTipo -> !subTiposSolicitados.contains(subTipo.getSubTipoAviso().getId()))
                .forEach(subTipo -> subTipo.setFechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso(ahora));

        for (Long subTipoAvisoId : tipoRequest.subTipoAvisoIds()) {
            if (subTiposActivos.containsKey(subTipoAvisoId)) {
                continue;
            }

            final SubTipoAviso subTipoAviso = buscarSubTipoValidoParaTipo(subTipoAvisoId, subTiposActivosCatalogo, tipoAviso);
            avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().add(AvisoTipoAvisoSubTipoAviso.builder()
                    .avisoTipoAviso(avisoTipoAviso)
                    .subTipoAviso(subTipoAviso)
                    .fechaHoraAsignacionAvisoTipoAvisoSubTipoAviso(ahora)
                    .build());
        }
    }

    private void desasignarTipoCompleto(AvisoTipoAviso avisoTipoAviso, Instant ahora) {
        avisoTipoAviso.setFechaHoraDesasignacionAvisoTipoAviso(ahora);
        avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().stream()
                .filter(subTipo -> subTipo.getFechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso() == null)
                .forEach(subTipo -> subTipo.setFechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso(ahora));
    }

    private AvisoResumenResponse toResumenResponse(Aviso aviso) {
        return new AvisoResumenResponse(
                aviso.getId(),
                aviso.getNombreAviso(),
                aviso.getDescripcionAviso(),
                aviso.getFechaCierreAviso(),
                getRazonSocialEmpresa(aviso),
                getNombreReclutador(aviso),
                mapCarreras(aviso),
                mapTipos(aviso)
        );
    }

    private AvisoDetalleResponse toDetalleResponse(Aviso aviso) {
        return new AvisoDetalleResponse(
                aviso.getId(),
                aviso.getNombreAviso(),
                aviso.getDescripcionAviso(),
                aviso.getFechaCierreAviso(),
                aviso.getFechaCreacionAviso(),
                aviso.getImagenUrlAviso(),
                getRazonSocialEmpresa(aviso),
                getNombreReclutador(aviso),
                mapCarreras(aviso),
                mapTipos(aviso)
        );
    }

    private List<String> mapCarreras(Aviso aviso) {
        return aviso.getAvisosCarrera().stream()
                .filter(avisoCarrera -> avisoCarrera.getFechaDesasignacionCarrera() == null)
                .sorted(Comparator.comparing(
                        AvisoCarrera::getPrioridadAvisoCarrera,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(AvisoCarrera::getCarrera)
                .filter(Objects::nonNull)
                .map(carrera -> carrera.getNombreCarrera())
                .distinct()
                .toList();
    }

    private List<AvisoTipoResponse> mapTipos(Aviso aviso) {
        return aviso.getAvisosTipoAvisos().stream()
                .filter(avisoTipoAviso -> avisoTipoAviso.getFechaHoraDesasignacionAvisoTipoAviso() == null)
                .map(this::toTipoResponse)
                .toList();
    }

    private AvisoTipoResponse toTipoResponse(AvisoTipoAviso avisoTipoAviso) {
        final List<String> subTipos = avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().stream()
                .filter(subTipo -> subTipo.getFechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso() == null)
                .map(AvisoTipoAvisoSubTipoAviso::getSubTipoAviso)
                .filter(Objects::nonNull)
                .map(subTipoAviso -> subTipoAviso.getNombreSubTipoAviso())
                .distinct()
                .toList();

        return new AvisoTipoResponse(
                avisoTipoAviso.getTipoAviso() != null
                        ? avisoTipoAviso.getTipoAviso().getNombreTipoAviso()
                        : null,
                subTipos
        );
    }

    private AvisoResponse toAvisoResponse(Aviso aviso) {
        final AvisoResponse.EstadoAvisoResponse estadoActual = aviso.getEstadoActual() != null
                ? new AvisoResponse.EstadoAvisoResponse(
                aviso.getEstadoActual().getId(),
                aviso.getEstadoActual().getCodigoInterno(),
                aviso.getEstadoActual().getNombreEstadoAviso()
        )
                : null;

        final AvisoResponse.EmpresaResponse empresa = aviso.getEmpresa() != null
                ? new AvisoResponse.EmpresaResponse(
                aviso.getEmpresa().getId(),
                aviso.getEmpresa().getCuitEmpresa(),
                aviso.getEmpresa().getRazonSocialEmpresa()
        )
                : null;

        final AvisoResponse.ReclutadorResponse reclutador = aviso.getReclutador() != null
                ? new AvisoResponse.ReclutadorResponse(
                aviso.getReclutador().getId(),
                aviso.getReclutador().getCuilReclutador(),
                aviso.getReclutador().getNombreReclutador()
        )
                : null;

        final List<AvisoResponse.CarreraResponse> carreras = aviso.getAvisosCarrera().stream()
                .filter(avisoCarrera -> avisoCarrera.getFechaDesasignacionCarrera() == null)
                .sorted(Comparator.comparing(
                        AvisoCarrera::getPrioridadAvisoCarrera,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(avisoCarrera -> new AvisoResponse.CarreraResponse(
                        avisoCarrera.getCarrera().getId(),
                        avisoCarrera.getCarrera().getNombreCarrera(),
                        avisoCarrera.getPrioridadAvisoCarrera()
                ))
                .toList();

        final List<AvisoResponse.TipoAvisoResponse> tiposAviso = aviso.getAvisosTipoAvisos().stream()
                .filter(avisoTipoAviso -> avisoTipoAviso.getFechaHoraDesasignacionAvisoTipoAviso() == null)
                .map(avisoTipoAviso -> new AvisoResponse.TipoAvisoResponse(
                        avisoTipoAviso.getTipoAviso().getId(),
                        avisoTipoAviso.getTipoAviso().getNombreTipoAviso(),
                        avisoTipoAviso.getAvisosTipoAvisosSubTiposAvisos().stream()
                                .filter(subTipo -> subTipo.getFechaHoraDesasignacionAvisoTipoAvisoSubTipoAviso() == null)
                                .map(AvisoTipoAvisoSubTipoAviso::getSubTipoAviso)
                                .filter(Objects::nonNull)
                                .map(subTipoAviso -> new AvisoResponse.SubTipoAvisoResponse(
                                        subTipoAviso.getId(),
                                        subTipoAviso.getNombreSubTipoAviso()
                                ))
                                .toList()
                ))
                .toList();

        return new AvisoResponse(
                aviso.getId(),
                aviso.getNombreAviso(),
                aviso.getDescripcionAviso(),
                aviso.getFechaCreacionAviso(),
                aviso.getFechaPublicacionAviso(),
                aviso.getFechaCierreAviso(),
                aviso.getFechaBajaAviso(),
                aviso.getImagenUrlAviso(),
                estadoActual,
                empresa,
                reclutador,
                carreras,
                tiposAviso
        );
    }

    private String getRazonSocialEmpresa(Aviso aviso) {
        return aviso.getEmpresa() != null ? aviso.getEmpresa().getRazonSocialEmpresa() : null;
    }

    private String getNombreReclutador(Aviso aviso) {
        return aviso.getReclutador() != null ? aviso.getReclutador().getNombreReclutador() : null;
    }
}

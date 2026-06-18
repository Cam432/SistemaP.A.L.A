package com.facultad.sistemaavisos.security;

import com.facultad.sistemaavisos.postulante.PostulanteRepository;
import com.facultad.sistemaavisos.reclutador.ReclutadorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("authorizationService")
public class AuthorizationService {

    private final ReclutadorRepository reclutadorRepository;
    private final PostulanteRepository postulanteRepository;

    public AuthorizationService(
            ReclutadorRepository reclutadorRepository,
            PostulanteRepository postulanteRepository
    ) {
        this.reclutadorRepository = reclutadorRepository;
        this.postulanteRepository = postulanteRepository;
    }

    public boolean esReclutador(Long reclutadorId) {
        final String mailAutenticado = obtenerMailAutenticado();
        if (mailAutenticado == null) {
            return false;
        }

        return reclutadorRepository.findByIdAndFechaBajaReclutadorIsNull(reclutadorId)
                .map(reclutador -> mailAutenticado.equalsIgnoreCase(reclutador.getMailReclutador()))
                .orElse(false);
    }

    public boolean esPostulante(Long postulanteId) {
        final String mailAutenticado = obtenerMailAutenticado();
        if (mailAutenticado == null) {
            return false;
        }

        return postulanteRepository.findByIdAndFechaBajaPostulanteIsNull(postulanteId)
                .map(postulante ->
                        mailAutenticado.equalsIgnoreCase(postulante.getMailAcademicoPostulante()) ||
                                (postulante.getMailPersonalPostulante() != null &&
                                        mailAutenticado.equalsIgnoreCase(postulante.getMailPersonalPostulante()))
                )
                .orElse(false);
    }

    private String obtenerMailAutenticado() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        return authentication.getName();
    }
}

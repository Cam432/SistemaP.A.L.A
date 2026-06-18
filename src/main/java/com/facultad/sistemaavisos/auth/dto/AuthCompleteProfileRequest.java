package com.facultad.sistemaavisos.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record AuthCompleteProfileRequest(
        ReclutadorProfileData reclutador,
        PostulanteProfileData postulante
) {

    public record ReclutadorProfileData(
            @NotBlank String nombreReclutador,
            @NotBlank String cuilReclutador,
            String descripcionReclutador
    ) {
    }

    public record PostulanteProfileData(
            @NotBlank String nombrePostulante,
            @NotBlank String apellidoPostulante,
            Instant fechaNacimientoPostulante,
            @NotNull @Positive Long legajoAcademicoPostulante,
            @Email @NotBlank String mailAcademicoPostulante,
            String mailPersonalPostulante,
            @NotNull @Positive Long tipoEstudianteId
    ) {
    }
}

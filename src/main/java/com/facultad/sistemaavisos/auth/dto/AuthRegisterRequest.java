package com.facultad.sistemaavisos.auth.dto;

import com.facultad.sistemaavisos.auth.PalaRol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record AuthRegisterRequest(
        @Email @NotBlank String mailUsuario,
        @NotBlank String passwordUsuario,
        @NotNull PalaRol rolSolicitado,
        ReclutadorRegisterData reclutador,
        PostulanteRegisterData postulante
) {

    public record ReclutadorRegisterData(
            @NotBlank String nombreReclutador,
            @NotBlank String cuilReclutador,
            String descripcionReclutador
    ) {
    }

    public record PostulanteRegisterData(
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

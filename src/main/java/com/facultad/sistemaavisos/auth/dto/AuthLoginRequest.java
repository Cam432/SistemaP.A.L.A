package com.facultad.sistemaavisos.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @Email @NotBlank String mailUsuario,
        @NotBlank String passwordUsuario
) {
}

package com.facultad.sistemaavisos.auth.dto;

public record SecuritySubsystemExternalRegisterRequest(
        String mailUsuario,
        String passwordUsuarioSistema,
        String rolSolicitado
) {
}

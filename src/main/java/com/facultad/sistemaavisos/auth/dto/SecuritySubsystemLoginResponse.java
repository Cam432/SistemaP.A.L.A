package com.facultad.sistemaavisos.auth.dto;

import java.util.List;

public record SecuritySubsystemLoginResponse(
        String token,
        String tipo,
        Long usuarioId,
        String mailUsuario,
        String systemKey,
        List<String> roles,
        List<String> permisos
) {
}

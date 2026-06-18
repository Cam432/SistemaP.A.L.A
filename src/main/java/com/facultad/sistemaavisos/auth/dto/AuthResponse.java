package com.facultad.sistemaavisos.auth.dto;

import java.util.List;

public record AuthResponse(
        String token,
        String tipo,
        Long usuarioId,
        String mailUsuario,
        List<String> roles,
        List<String> permisos,
        boolean perfilCompleto,
        String perfilPendiente
) {
}

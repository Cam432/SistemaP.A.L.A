package com.facultad.sistemaavisos.auth;

import com.facultad.sistemaavisos.auth.dto.AuthLoginRequest;
import com.facultad.sistemaavisos.auth.dto.AuthCompleteProfileRequest;
import com.facultad.sistemaavisos.auth.dto.AuthRegisterRequest;
import com.facultad.sistemaavisos.auth.dto.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthLoginRequest request);

    AuthResponse register(AuthRegisterRequest request);

    AuthResponse completarPerfilInicial(String bearerToken, AuthCompleteProfileRequest request);

    AuthResponse construirSesionActual(String bearerToken, String mailUsuario);
}

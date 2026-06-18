package com.facultad.sistemaavisos.auth;

import com.facultad.sistemaavisos.auth.dto.AuthLoginRequest;
import com.facultad.sistemaavisos.auth.dto.AuthCompleteProfileRequest;
import com.facultad.sistemaavisos.auth.dto.AuthRegisterRequest;
import com.facultad.sistemaavisos.auth.dto.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PatchMapping("/perfil-inicial")
    public ResponseEntity<AuthResponse> completarPerfilInicial(
            Authentication authentication,
            @RequestBody @Valid AuthCompleteProfileRequest request
    ) {
        final String bearer = authentication == null ? null : authentication.getCredentials() instanceof String
                ? (String) authentication.getCredentials()
                : null;

        return ResponseEntity.ok(authService.completarPerfilInicial(bearer, request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        final String bearer = authentication == null ? null : authentication.getCredentials() instanceof String
                ? (String) authentication.getCredentials()
                : null;
        return ResponseEntity.ok(authService.construirSesionActual(
                bearer,
                authentication != null ? authentication.getName() : null
        ));
    }
}

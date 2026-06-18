package com.facultad.sistemaavisos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JwtService {

    private final String secretKey;

    public JwtService(@Value("${security.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extraerMail(String token) {
        return extraerClaims(token).get("mail", String.class);
    }

    public Long extraerSubjectId(String token) {
        return Long.valueOf(extraerClaims(token).getSubject());
    }

    @SuppressWarnings("unchecked")
    public List<String> extraerRoles(String token) {
        final List<String> roles = extraerClaims(token).get("roles", List.class);
        return roles == null ? List.of() : roles;
    }

    @SuppressWarnings("unchecked")
    public List<String> extraerPermisos(String token) {
        final List<String> permisos = extraerClaims(token).get("permisos", List.class);
        return permisos == null ? List.of() : permisos;
    }

    public boolean tokenValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        final byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

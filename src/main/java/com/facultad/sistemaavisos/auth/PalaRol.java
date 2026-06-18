package com.facultad.sistemaavisos.auth;

public enum PalaRol {
    POSTULANTE("Postulante"),
    RECLUTADOR("Reclutador"),
    ADMINISTRADOR("Administrador");

    private final String nombreVisible;

    PalaRol(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getClave() {
        return name();
    }

    public String getNombreVisible() {
        return nombreVisible;
    }
}

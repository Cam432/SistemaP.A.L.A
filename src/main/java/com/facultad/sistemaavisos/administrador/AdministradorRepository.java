package com.facultad.sistemaavisos.administrador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByUsuarioSeguridadId(Long usuarioSeguridadId);

    Optional<Administrador> findByMailAdministradorAndFechaBajaAdministradorIsNull(String mailAdministrador);
}

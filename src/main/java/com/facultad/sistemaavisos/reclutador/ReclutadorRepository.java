package com.facultad.sistemaavisos.reclutador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReclutadorRepository extends JpaRepository<Reclutador, Long> {

    Optional<Reclutador> findByCuilReclutador(String cuilReclutador);

    Optional<Reclutador> findByIdAndFechaBajaReclutadorIsNull(Long id);

    Optional<Reclutador> findByMailReclutadorAndFechaBajaReclutadorIsNull(String mailReclutador);

    Optional<Reclutador> findByUsuarioSeguridadId(Long usuarioSeguridadId);
}

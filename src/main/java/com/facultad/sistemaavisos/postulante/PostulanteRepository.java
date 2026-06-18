package com.facultad.sistemaavisos.postulante;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostulanteRepository extends JpaRepository<Postulante, Long> {

    Optional<Postulante> findByIdAndFechaBajaPostulanteIsNull(Long id);

    Optional<Postulante> findByUsuarioSeguridadId(Long usuarioSeguridadId);

    Optional<Postulante> findByMailAcademicoPostulanteAndFechaBajaPostulanteIsNull(String mailAcademicoPostulante);

    Optional<Postulante> findByMailPersonalPostulanteAndFechaBajaPostulanteIsNull(String mailPersonalPostulante);
}

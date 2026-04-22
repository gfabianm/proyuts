package com.proyuts.app.repository;

import com.proyuts.app.entity.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);
}
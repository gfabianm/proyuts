package com.proyuts.app.service;

import com.proyuts.app.entity.Inscripcion;
import com.proyuts.app.repository.InscripcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    public String inscribir(Long usuarioId, Long cursoId) {

        boolean yaExiste = inscripcionRepository
                .existsByUsuarioIdAndCursoId(usuarioId, cursoId);

        if (yaExiste) {
            return "YA INSCRITO";
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuarioId(usuarioId);
        inscripcion.setCursoId(cursoId);
        inscripcion.setFechaInscripcion(LocalDateTime.now());

        inscripcionRepository.save(inscripcion);

        return "INSCRIPCION EXITOSA";
    }
}
package com.proyuts.app.service;

import com.proyuts.app.entity.Curso;
import com.proyuts.app.entity.Inscripcion;
import com.proyuts.app.entity.Usuario;
import com.proyuts.app.repository.CursoRepository;
import com.proyuts.app.repository.InscripcionRepository;
import com.proyuts.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public InscripcionService(
            InscripcionRepository inscripcionRepository,
            CursoRepository cursoRepository,
            UsuarioRepository usuarioRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public String inscribir(Long usuarioId, Long cursoId) {

        Curso curso = cursoRepository.findById(cursoId).orElse(null);

        if (curso == null) {
            return "El curso no existe";
        }

        boolean yaExiste = inscripcionRepository.existsByUsuarioIdAndCursoId(usuarioId, cursoId);

        if (yaExiste) {
            return "Ya estás inscrito en este curso";
        }

        long inscritos = inscripcionRepository.countByCursoId(cursoId);

        if (curso.getCupoMaximo() != null && inscritos >= curso.getCupoMaximo()) {
            return "El curso ya está lleno";
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuarioId(usuarioId);
        inscripcion.setCursoId(cursoId);
        inscripcion.setFechaInscripcion(LocalDateTime.now());

        inscripcionRepository.save(inscripcion);

        return "Inscripción exitosa";
    }

    public void cancelarInscripcion(Long usuarioId, Long cursoId) {

        Inscripcion inscripcion = inscripcionRepository
                .findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .orElse(null);

        if (inscripcion != null) {
            inscripcionRepository.delete(inscripcion);
        }
    }

    public List<Curso> obtenerCursosPorUsuario(Long usuarioId) {

        List<Inscripcion> inscripciones = inscripcionRepository.findByUsuarioId(usuarioId);

        List<Curso> cursos = new ArrayList<>();

        for (Inscripcion inscripcion : inscripciones) {
            Curso curso = cursoRepository.findById(inscripcion.getCursoId()).orElse(null);

            if (curso != null) {
                cursos.add(curso);
            }
        }

        return cursos;
    }

    public List<Usuario> obtenerEstudiantesPorCurso(Long cursoId) {

        List<Inscripcion> inscripciones = inscripcionRepository.findAll();

        List<Usuario> estudiantes = new ArrayList<>();

        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getCursoId().equals(cursoId)) {
                Usuario usuario = usuarioRepository.findById(inscripcion.getUsuarioId()).orElse(null);

                if (usuario != null) {
                    estudiantes.add(usuario);
                }
            }
        }

        return estudiantes;
    }

    public int calcularProyutsAcumulados(Long usuarioId) {

        List<Curso> cursos = obtenerCursosPorUsuario(usuarioId);

        int total = 0;

        for (Curso curso : cursos) {
            if (curso.getProyutsOtorgados() != null) {
                total += curso.getProyutsOtorgados();
            }
        }

        return total;
    }

    public int calcularProyutsFaltantes(Long usuarioId) {

        int meta = 52;
        int acumulados = calcularProyutsAcumulados(usuarioId);

        int faltantes = meta - acumulados;

        if (faltantes < 0) {
            return 0;
        }

        return faltantes;
    }

    public int calcularPorcentajeAvance(Long usuarioId) {

        int meta = 52;
        int acumulados = calcularProyutsAcumulados(usuarioId);

        int porcentaje = (acumulados * 100) / meta;

        if (porcentaje > 100) {
            return 100;
        }

        return porcentaje;
    }

    public int calcularProyutsPorRango(Long usuarioId, LocalDateTime inicio, LocalDateTime fin) {

        List<Inscripcion> inscripciones = inscripcionRepository.findByUsuarioId(usuarioId);

        int total = 0;

        for (Inscripcion inscripcion : inscripciones) {

            LocalDateTime fecha = inscripcion.getFechaInscripcion();

            if (fecha != null && !fecha.isBefore(inicio) && !fecha.isAfter(fin)) {

                Curso curso = cursoRepository.findById(inscripcion.getCursoId()).orElse(null);

                if (curso != null && curso.getProyutsOtorgados() != null) {
                    total += curso.getProyutsOtorgados();
                }
            }
        }

        return total;
    }

    public int calcularProyutsSemana(Long usuarioId) {

        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(6);

        return calcularProyutsPorRango(
                usuarioId,
                inicioSemana.atStartOfDay(),
                hoy.atTime(23, 59, 59)
        );
    }

    public int calcularProyutsMes(Long usuarioId) {

        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        return calcularProyutsPorRango(
                usuarioId,
                inicioMes.atStartOfDay(),
                hoy.atTime(23, 59, 59)
        );
    }

    public int calcularProyutsSemestre(Long usuarioId) {

        LocalDate hoy = LocalDate.now();

        int mes = hoy.getMonthValue();
        LocalDate inicioSemestre;

        if (mes <= 6) {
            inicioSemestre = LocalDate.of(hoy.getYear(), 1, 1);
        } else {
            inicioSemestre = LocalDate.of(hoy.getYear(), 7, 1);
        }

        return calcularProyutsPorRango(
                usuarioId,
                inicioSemestre.atStartOfDay(),
                hoy.atTime(23, 59, 59)
        );
    }

    public int calcularProyutsAnio(Long usuarioId) {

        LocalDate hoy = LocalDate.now();
        LocalDate inicioAnio = LocalDate.of(hoy.getYear(), 1, 1);

        return calcularProyutsPorRango(
                usuarioId,
                inicioAnio.atStartOfDay(),
                hoy.atTime(23, 59, 59)
        );
    }
}
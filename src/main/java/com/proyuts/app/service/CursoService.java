package com.proyuts.app.service;

import com.proyuts.app.entity.Curso;
import com.proyuts.app.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    public List<Curso> filtrarCursos(String nombre, Long categoriaId, Long estadoId) {

        boolean tieneNombre = nombre != null && !nombre.trim().isEmpty();
        boolean tieneCategoria = categoriaId != null;
        boolean tieneEstado = estadoId != null;

        if (tieneNombre && tieneCategoria && tieneEstado) {
            return cursoRepository.findByNombreContainingIgnoreCaseAndCategoriaIdAndEstadoId(
                    nombre,
                    categoriaId,
                    estadoId
            );
        }

        if (tieneNombre && tieneCategoria) {
            return cursoRepository.findByNombreContainingIgnoreCaseAndCategoriaId(nombre, categoriaId);
        }

        if (tieneNombre && tieneEstado) {
            return cursoRepository.findByNombreContainingIgnoreCaseAndEstadoId(nombre, estadoId);
        }

        if (tieneCategoria && tieneEstado) {
            return cursoRepository.findByCategoriaIdAndEstadoId(categoriaId, estadoId);
        }

        if (tieneNombre) {
            return cursoRepository.findByNombreContainingIgnoreCase(nombre);
        }

        if (tieneCategoria) {
            return cursoRepository.findByCategoriaId(categoriaId);
        }

        if (tieneEstado) {
            return cursoRepository.findByEstadoId(estadoId);
        }

        return cursoRepository.findAll();
    }

    public List<Curso> obtenerTop5Cursos() {
        return cursoRepository.findTop5ByOrderByProyutsOtorgadosDesc();
    }

    public List<Curso> obtenerCursosPorProfesor(Long profesorId) {
        return cursoRepository.findByProfesorId(profesorId);
    }
}
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
}
package com.proyuts.app.repository;

import com.proyuts.app.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByNombreContainingIgnoreCase(String nombre);

    List<Curso> findByCategoriaId(Long categoriaId);

    List<Curso> findByEstadoId(Long estadoId);

    List<Curso> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);

    List<Curso> findByNombreContainingIgnoreCaseAndEstadoId(String nombre, Long estadoId);

    List<Curso> findByCategoriaIdAndEstadoId(Long categoriaId, Long estadoId);

    List<Curso> findByNombreContainingIgnoreCaseAndCategoriaIdAndEstadoId(
            String nombre,
            Long categoriaId,
            Long estadoId
    );

    List<Curso> findTop5ByOrderByProyutsOtorgadosDesc();

    List<Curso> findByProfesorId(Long profesorId);
}
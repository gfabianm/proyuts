package com.proyuts.app.repository;

import com.proyuts.app.entity.EstadoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoCursoRepository extends JpaRepository<EstadoCurso, Long> {
}
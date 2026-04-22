package com.proyuts.app.repository;

import com.proyuts.app.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Rol findByNombre(String nombre);
}
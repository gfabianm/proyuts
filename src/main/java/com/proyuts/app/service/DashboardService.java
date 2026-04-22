package com.proyuts.app.service;

import com.proyuts.app.repository.CursoRepository;
import com.proyuts.app.repository.RolRepository;
import com.proyuts.app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final RolRepository rolRepository;

    public DashboardService(
            UsuarioRepository usuarioRepository,
            CursoRepository cursoRepository,
            RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.rolRepository = rolRepository;
    }

    public Map<String, Long> obtenerResumen() {
        Map<String, Long> resumen = new HashMap<>();
        resumen.put("usuarios", usuarioRepository.count());
        resumen.put("cursos", cursoRepository.count());
        resumen.put("roles", rolRepository.count());
        return resumen;
    }
}
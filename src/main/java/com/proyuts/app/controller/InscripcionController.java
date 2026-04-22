package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.InscripcionService;
import com.proyuts.app.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final UsuarioService usuarioService;

    public InscripcionController(
            InscripcionService inscripcionService,
            UsuarioService usuarioService) {
        this.inscripcionService = inscripcionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/inscribirse/{cursoId}")
    public String inscribirse(@PathVariable Long cursoId,
                             Authentication authentication) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        inscripcionService.inscribir(usuario.getId(), cursoId);

        return "redirect:/web/cursos";
    }
}
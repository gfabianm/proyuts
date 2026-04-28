package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.InscripcionService;
import com.proyuts.app.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String inscribirse(
            @PathVariable Long cursoId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        String resultado = inscripcionService.inscribir(usuario.getId(), cursoId);

        if ("Inscripción exitosa".equals(resultado)) {
            redirectAttributes.addFlashAttribute("mensajeExito", resultado);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", resultado);
        }

        return "redirect:/web/cursos";
    }
}
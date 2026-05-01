package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.entity.Curso; // Asegúrate de importar Curso
import com.proyuts.app.service.InscripcionService;
import com.proyuts.app.service.UsuarioService;
import com.proyuts.app.service.CursoService; // Necesitarás el service de cursos
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final UsuarioService usuarioService;
    private final CursoService cursoService; // Agregado para validar el curso

    public InscripcionController(
            InscripcionService inscripcionService,
            UsuarioService usuarioService,
            CursoService cursoService) {
        this.inscripcionService = inscripcionService;
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
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

        // VALIDACIÓN DE SEGURIDAD: Verificar estado antes de inscribir
        Curso curso = cursoService.buscarPorId(cursoId);
        if (curso == null || curso.getEstado().getId() != 1) {
            redirectAttributes.addFlashAttribute("mensajeError", "El curso no está disponible para inscripciones.");
            return "redirect:/web/cursos";
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
package com.proyuts.app.controller;

import com.proyuts.app.entity.Curso;
import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.CursoService;
import com.proyuts.app.service.InscripcionService;
import com.proyuts.app.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProfesorController {

    private final UsuarioService usuarioService;
    private final CursoService cursoService;
    private final InscripcionService inscripcionService;

    public ProfesorController(
            UsuarioService usuarioService,
            CursoService cursoService,
            InscripcionService inscripcionService) {
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/profesor/panel")
    public String verPanelProfesor(Authentication authentication, Model model) {

        String email = authentication.getName();

        Usuario profesor = usuarioService.buscarPorEmail(email);

        if (profesor == null) {
            return "redirect:/login?error";
        }

        List<Curso> cursos = cursoService.obtenerCursosPorProfesor(profesor.getId());

        model.addAttribute("profesor", profesor);
        model.addAttribute("cursos", cursos);
        model.addAttribute("totalCursos", cursos.size());

        return "profesor/panel";
    }

    @GetMapping("/profesor/curso/{cursoId}/estudiantes")
    public String verEstudiantesCurso(
            @PathVariable Long cursoId,
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        Usuario profesor = usuarioService.buscarPorEmail(email);

        if (profesor == null) {
            return "redirect:/login?error";
        }

        Curso curso = cursoService.buscarPorId(cursoId);

        if (curso == null) {
            return "redirect:/profesor/panel";
        }

        if (curso.getProfesor() == null || !curso.getProfesor().getId().equals(profesor.getId())) {
            return "redirect:/403";
        }

        List<Usuario> estudiantes = inscripcionService.obtenerEstudiantesPorCurso(cursoId);

        model.addAttribute("profesor", profesor);
        model.addAttribute("curso", curso);
        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("totalEstudiantes", estudiantes.size());

        return "profesor/estudiantes-curso";
    }
}
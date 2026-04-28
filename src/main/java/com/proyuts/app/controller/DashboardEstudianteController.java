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

import java.util.List;

@Controller
public class DashboardEstudianteController {

    private final UsuarioService usuarioService;
    private final InscripcionService inscripcionService;
    private final CursoService cursoService;

    public DashboardEstudianteController(
            UsuarioService usuarioService,
            InscripcionService inscripcionService,
            CursoService cursoService) {
        this.usuarioService = usuarioService;
        this.inscripcionService = inscripcionService;
        this.cursoService = cursoService;
    }

    @GetMapping("/dashboard-estudiante")
    public String verDashboardEstudiante(Authentication authentication, Model model) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        List<Curso> cursos = inscripcionService.obtenerCursosPorUsuario(usuario.getId());

        int proyutsAcumulados = inscripcionService.calcularProyutsAcumulados(usuario.getId());
        int proyutsFaltantes = inscripcionService.calcularProyutsFaltantes(usuario.getId());
        int porcentajeAvance = inscripcionService.calcularPorcentajeAvance(usuario.getId());

        List<Curso> topCursos = cursoService.obtenerTop5Cursos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("cursos", cursos);
        model.addAttribute("totalCursos", cursos.size());
        model.addAttribute("proyutsAcumulados", proyutsAcumulados);
        model.addAttribute("proyutsFaltantes", proyutsFaltantes);
        model.addAttribute("porcentajeAvance", porcentajeAvance);
        model.addAttribute("topCursos", topCursos);

        return "dashboard-estudiante";
    }
}
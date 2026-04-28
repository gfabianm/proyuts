package com.proyuts.app.controller;

import com.proyuts.app.entity.Curso;
import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.InscripcionService;
import com.proyuts.app.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MisCursosController {

    private final InscripcionService inscripcionService;
    private final UsuarioService usuarioService;

    public MisCursosController(
            InscripcionService inscripcionService,
            UsuarioService usuarioService) {
        this.inscripcionService = inscripcionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mis-cursos")
    public String verMisCursos(Authentication authentication, Model model) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        List<Curso> cursos = inscripcionService.obtenerCursosPorUsuario(usuario.getId());

        int proyutsAcumulados = inscripcionService.calcularProyutsAcumulados(usuario.getId());
        int proyutsFaltantes = inscripcionService.calcularProyutsFaltantes(usuario.getId());
        int porcentajeAvance = inscripcionService.calcularPorcentajeAvance(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("cursos", cursos);
        model.addAttribute("proyutsAcumulados", proyutsAcumulados);
        model.addAttribute("proyutsFaltantes", proyutsFaltantes);
        model.addAttribute("porcentajeAvance", porcentajeAvance);

        return "mis-cursos";
    }

    @GetMapping("/mis-cursos/cancelar/{cursoId}")
    public String cancelarCurso(@PathVariable Long cursoId, Authentication authentication) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        inscripcionService.cancelarInscripcion(usuario.getId(), cursoId);

        return "redirect:/mis-cursos";
    }
}
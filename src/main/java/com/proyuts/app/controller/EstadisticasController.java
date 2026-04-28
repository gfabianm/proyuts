package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.InscripcionService;
import com.proyuts.app.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadisticasController {

    private final UsuarioService usuarioService;
    private final InscripcionService inscripcionService;

    public EstadisticasController(
            UsuarioService usuarioService,
            InscripcionService inscripcionService) {
        this.usuarioService = usuarioService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/estadisticas")
    public String verEstadisticas(Authentication authentication, Model model) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        int semana = inscripcionService.calcularProyutsSemana(usuario.getId());
        int mes = inscripcionService.calcularProyutsMes(usuario.getId());
        int semestre = inscripcionService.calcularProyutsSemestre(usuario.getId());
        int anio = inscripcionService.calcularProyutsAnio(usuario.getId());
        int total = inscripcionService.calcularProyutsAcumulados(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("semana", semana);
        model.addAttribute("mes", mes);
        model.addAttribute("semestre", semestre);
        model.addAttribute("anio", anio);
        model.addAttribute("total", total);

        return "estadisticas";
    }
}
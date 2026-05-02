package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalculadoraController {

    private final UsuarioService usuarioService;

    public CalculadoraController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /calculadora-notas
     * Vista exclusiva para estudiantes. Solo muestra la pantalla;
     * toda la lógica de cálculo es 100% JavaScript en el cliente.
     */
    @GetMapping("/calculadora")
    public String mostrarCalculadora(Authentication authentication, Model model) {

        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("usuario", usuario);
        return "calculadora-notas";   // → templates/calculadora-notas.html
    }
}
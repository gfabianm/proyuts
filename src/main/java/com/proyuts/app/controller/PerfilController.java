package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String mostrarFormularioEditar(Authentication authentication, Model model) {

        String email = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (usuario == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("usuario", usuario);

        return "editar-perfil";
    }

    @PostMapping("/perfil/editar")
    public String actualizarPerfil(
            @Valid Usuario usuario,
            BindingResult result,
            Authentication authentication,
            Model model) {

        if (result.hasFieldErrors("nombre")
                || result.hasFieldErrors("apellido")
                || result.hasFieldErrors("programaAcademico")) {
            model.addAttribute("usuario", usuario);
            return "editar-perfil";
        }

        String email = authentication.getName();

        usuarioService.actualizarPerfil(email, usuario);

        return "redirect:/perfil?actualizado";
    }

    @GetMapping("/perfil/cambiar-password")
    public String mostrarFormularioPassword() {
        return "cambiar-password";
    }

    @PostMapping("/perfil/cambiar-password")
    public String cambiarPassword(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            @RequestParam String confirmarPasswordNueva,
            Authentication authentication,
            Model model) {

        try {
            String email = authentication.getName();

            usuarioService.cambiarPassword(
                    email,
                    passwordActual,
                    passwordNueva,
                    confirmarPasswordNueva
            );

            return "redirect:/perfil?passwordActualizada";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "cambiar-password";
        }
    }
}
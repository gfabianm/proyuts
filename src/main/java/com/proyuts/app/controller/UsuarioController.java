package com.proyuts.app.controller;

import com.proyuts.app.entity.Usuario;
import com.proyuts.app.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/buscar")
    public Usuario buscarPorEmail(@RequestParam String email) {
        return usuarioService.buscarPorEmail(email);
    }

    @GetMapping("/admin/test")
    public String admin() {
        return "Solo ADMIN puede ver esto";
    }

    @GetMapping("/profesor/test")
    public String profesor() {
        return "Solo PROFESOR puede ver esto";
    }

    @GetMapping("/estudiante/test")
    public String estudiante() {
        return "Solo ESTUDIANTE puede ver esto";
    }
}
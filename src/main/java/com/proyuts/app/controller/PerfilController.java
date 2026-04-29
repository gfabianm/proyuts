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
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import net.coobird.thumbnailator.Thumbnails;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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
        
        if (usuario == null) return "redirect:/login?error";

        model.addAttribute("usuario", usuario);
        model.addAttribute("fotoBase64", convertirABase64(usuario.getFoto()));
        
        return "perfil";
    }

    @GetMapping("/perfil/editar")
    public String mostrarFormularioEditar(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) return "redirect:/login?error";
        
        model.addAttribute("usuario", usuario);
        return "editar-perfil";
    }

    @PostMapping("/perfil/editar")
    public String actualizarPerfil(
            @Valid Usuario usuarioFormulario,
            BindingResult result,
            @RequestParam("archivoImagen") MultipartFile archivo,
            Authentication authentication,
            Model model) {

        // 1. Validar errores (ignorando email y password)
        boolean tieneErroresReales = result.getFieldErrors().stream()
                .anyMatch(f -> !f.getField().equals("email") && !f.getField().equals("password"));

        if (tieneErroresReales) {
            model.addAttribute("usuario", usuarioFormulario);
            return "editar-perfil";
        }

        try {
            String emailActual = authentication.getName();
            Usuario usuarioDB = usuarioService.buscarPorEmail(emailActual);

            if (usuarioDB == null) return "redirect:/login?error";

            // 2. Actualizar datos de texto
            usuarioDB.setNombre(usuarioFormulario.getNombre());
            usuarioDB.setApellido(usuarioFormulario.getApellido());
            usuarioDB.setTelefono(usuarioFormulario.getTelefono());
            usuarioDB.setProgramaAcademico(usuarioFormulario.getProgramaAcademico());
            usuarioDB.setBiografia(usuarioFormulario.getBiografia());

            // 3. Procesar imagen CON compresión
            if (!archivo.isEmpty()) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(archivo.getBytes());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                Thumbnails.of(inputStream)
                    .size(400, 400)
                    .outputFormat("jpg")
                    .outputQuality(0.75)
                    .toOutputStream(outputStream);

                usuarioDB.setFoto(outputStream.toByteArray());
                System.out.println("Imagen comprimida con éxito.");
            }

            // 4. Guardar una sola vez al final del proceso
            usuarioService.guardar(usuarioDB);
            System.out.println("¡Cambios guardados con éxito!");

        } catch (IOException e) {
            System.out.println("Error procesando imagen: " + e.getMessage());
            model.addAttribute("error", "Error al procesar la imagen seleccionada.");
            model.addAttribute("usuario", usuarioFormulario);
            return "editar-perfil";
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
            model.addAttribute("error", "Ocurrió un error inesperado.");
            model.addAttribute("usuario", usuarioFormulario);
            return "editar-perfil";
        }

        return "redirect:/perfil?actualizado";
    }

    private String convertirABase64(byte[] contenido) {
        if (contenido == null || contenido.length == 0) return null;
        return Base64.getEncoder().encodeToString(contenido);
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
            usuarioService.cambiarPassword(authentication.getName(), passwordActual, passwordNueva, confirmarPasswordNueva);
            return "redirect:/perfil?passwordActualizada";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "cambiar-password";
        }
    }
}
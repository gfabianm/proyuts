package com.proyuts.app.service;

import com.proyuts.app.entity.Rol;
import com.proyuts.app.entity.Usuario;
import com.proyuts.app.repository.RolRepository;
import com.proyuts.app.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void registrarEstudiante(Usuario usuario, String confirmarPassword) {

        if (!usuario.getPassword().equals(confirmarPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente != null) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Rol rolEstudiante = rolRepository.findByNombre("ESTUDIANTE");

        if (rolEstudiante == null) {
            throw new RuntimeException("No existe el rol ESTUDIANTE en la base de datos");
        }

        Set<Rol> roles = new HashSet<>();
        roles.add(rolEstudiante);

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRoles(roles);
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        // CORRECCIÓN: Como ahora es byte[], simplemente verificamos si es null
        if (usuario.getFoto() == null) {
            usuario.setFoto(null); // O podrías cargar un archivo por defecto aquí si quisieras
        }

        usuarioRepository.save(usuario);
    }

    public void actualizarPerfil(String email, Usuario datosActualizados) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuario.setNombre(datosActualizados.getNombre());
        usuario.setApellido(datosActualizados.getApellido());
        usuario.setTelefono(datosActualizados.getTelefono());
        usuario.setProgramaAcademico(datosActualizados.getProgramaAcademico());
        usuario.setBiografia(datosActualizados.getBiografia());
        
        // CORRECCIÓN: También actualizamos la foto si el objeto trae una nueva
        if (datosActualizados.getFoto() != null) {
            usuario.setFoto(datosActualizados.getFoto());
        }

        usuarioRepository.save(usuario);
    }

    public void cambiarPassword(
            String email,
            String passwordActual,
            String passwordNueva,
            String confirmarPasswordNueva) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual no es correcta");
        }

        if (passwordNueva == null || passwordNueva.length() < 6) {
            throw new RuntimeException("La nueva contraseña debe tener mínimo 6 caracteres");
        }

        if (!passwordNueva.equals(confirmarPasswordNueva)) {
            throw new RuntimeException("La confirmación de contraseña no coincide");
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
    }

    public void cambiarEstadoUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuario.setActivo(!usuario.getActivo());

        usuarioRepository.save(usuario);
    }
}
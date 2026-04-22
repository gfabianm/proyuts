package com.proyuts.app.config;

import com.proyuts.app.entity.Categoria;
import com.proyuts.app.entity.Curso;
import com.proyuts.app.entity.EstadoCurso;
import com.proyuts.app.entity.Rol;
import com.proyuts.app.entity.Usuario;
import com.proyuts.app.repository.CategoriaRepository;
import com.proyuts.app.repository.CursoRepository;
import com.proyuts.app.repository.EstadoCursoRepository;
import com.proyuts.app.repository.RolRepository;
import com.proyuts.app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner cargarDatosIniciales(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            CursoRepository cursoRepository,
            CategoriaRepository categoriaRepository,
            EstadoCursoRepository estadoCursoRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            Rol rolEstudiante = rolRepository.findByNombre("ESTUDIANTE");

            if (rolEstudiante == null) {
                rolEstudiante = new Rol("ESTUDIANTE");
                rolRepository.save(rolEstudiante);
            }

            String emailPrueba = "admin@proyuts.com";

            Usuario usuario = usuarioRepository.findByEmail(emailPrueba);

            if (usuario == null) {
                usuario = new Usuario();
                usuario.setNombre("Fabian");
                usuario.setApellido("Gelvez");
                usuario.setEmail(emailPrueba);
                usuario.setPassword(passwordEncoder.encode("123456"));
                usuario.setTelefono("3000000000");
                usuario.setProgramaAcademico("Ingenieria de Software");
                usuario.setBiografia("Usuario de prueba");
                usuario.setFoto("default.png");
                usuario.setActivo(true);
            } else {
                usuario.setPassword(passwordEncoder.encode("123456"));
            }

            Set<Rol> roles = usuario.getRoles();

            if (roles == null) {
                roles = new HashSet<>();
            }

            roles.add(rolEstudiante);
            usuario.setRoles(roles);

            usuarioRepository.save(usuario);

            if (cursoRepository.count() == 0) {

                Categoria categoriaWeb = categoriaRepository.findById(3L).orElse(null);
                Categoria categoriaMoviles = categoriaRepository.findById(4L).orElse(null);
                Categoria categoriaPoo = categoriaRepository.findById(5L).orElse(null);
                EstadoCurso estadoDisponible = estadoCursoRepository.findById(1L).orElse(null);

                if (categoriaWeb != null && categoriaMoviles != null && categoriaPoo != null && estadoDisponible != null) {

                    Curso curso1 = new Curso();
                    curso1.setNombre("Programacion Web");
                    curso1.setDescripcion("Curso orientado al desarrollo de aplicaciones web");
                    curso1.setCupoMaximo(30);
                    curso1.setProyutsOtorgados(10);
                    curso1.setCategoria(categoriaWeb);
                    curso1.setEstado(estadoDisponible);
                    curso1.setFechaCreacion(LocalDateTime.now());

                    Curso curso2 = new Curso();
                    curso2.setNombre("Aplicaciones Moviles");
                    curso2.setDescripcion("Curso enfocado en desarrollo móvil");
                    curso2.setCupoMaximo(25);
                    curso2.setProyutsOtorgados(12);
                    curso2.setCategoria(categoriaMoviles);
                    curso2.setEstado(estadoDisponible);
                    curso2.setFechaCreacion(LocalDateTime.now());

                    Curso curso3 = new Curso();
                    curso3.setNombre("POO");
                    curso3.setDescripcion("Curso de programación orientada a objetos");
                    curso3.setCupoMaximo(35);
                    curso3.setProyutsOtorgados(8);
                    curso3.setCategoria(categoriaPoo);
                    curso3.setEstado(estadoDisponible);
                    curso3.setFechaCreacion(LocalDateTime.now());

                    cursoRepository.save(curso1);
                    cursoRepository.save(curso2);
                    cursoRepository.save(curso3);

                    System.out.println("=====================================");
                    System.out.println(" CURSOS DE PRUEBA GUARDADOS ");
                    System.out.println("=====================================");
                } else {
                    System.out.println("=====================================");
                    System.out.println(" NO SE ENCONTRARON CATEGORIAS O ESTADO ");
                    System.out.println("=====================================");
                }

            } else {
                System.out.println("=====================================");
                System.out.println(" YA EXISTEN CURSOS EN LA BASE DE DATOS ");
                System.out.println("=====================================");
            }

            System.out.println("=====================================");
            System.out.println(" USUARIO Y ROL LISTOS PARA LOGIN ");
            System.out.println("=====================================");
        };
    }
}
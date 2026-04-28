package com.proyuts.app.controller;

import com.proyuts.app.entity.Curso;
import com.proyuts.app.repository.CategoriaRepository;
import com.proyuts.app.repository.EstadoCursoRepository;
import com.proyuts.app.repository.UsuarioRepository;
import com.proyuts.app.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/web/cursos")
public class CursoWebController {

    private final CursoService cursoService;
    private final CategoriaRepository categoriaRepository;
    private final EstadoCursoRepository estadoCursoRepository;
    private final UsuarioRepository usuarioRepository;

    public CursoWebController(
            CursoService cursoService,
            CategoriaRepository categoriaRepository,
            EstadoCursoRepository estadoCursoRepository,
            UsuarioRepository usuarioRepository) {

        this.cursoService = cursoService;
        this.categoriaRepository = categoriaRepository;
        this.estadoCursoRepository = estadoCursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listarCursos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long estadoId,
            Model model) {

        model.addAttribute("cursos", cursoService.filtrarCursos(nombre, categoriaId, estadoId));
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("estados", estadoCursoRepository.findAll());

        model.addAttribute("nombre", nombre);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("estadoId", estadoId);

        return "cursos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("estados", estadoCursoRepository.findAll());
        model.addAttribute("profesores", usuarioRepository.findByRolesNombre("PROFESOR"));
        return "cursos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCurso(
            @Valid Curso curso,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("estados", estadoCursoRepository.findAll());
            model.addAttribute("profesores", usuarioRepository.findByRolesNombre("PROFESOR"));
            return "cursos/formulario";
        }

        if (curso.getFechaCreacion() == null) {
            curso.setFechaCreacion(LocalDateTime.now());
        }

        cursoService.guardar(curso);

        return "redirect:/web/cursos";
    }

    @GetMapping("/editar/{id}")
    public String editarCurso(@PathVariable Long id, Model model) {

        Curso curso = cursoService.buscarPorId(id);

        if (curso == null) {
            return "redirect:/web/cursos";
        }

        model.addAttribute("curso", curso);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("estados", estadoCursoRepository.findAll());
        model.addAttribute("profesores", usuarioRepository.findByRolesNombre("PROFESOR"));

        return "cursos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable Long id) {

        Curso curso = cursoService.buscarPorId(id);

        if (curso != null) {
            cursoService.eliminar(id);
        }

        return "redirect:/web/cursos";
    }
}
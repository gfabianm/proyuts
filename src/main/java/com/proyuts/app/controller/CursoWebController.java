package com.proyuts.app.controller;

import com.proyuts.app.entity.Curso;
import com.proyuts.app.repository.CategoriaRepository;
import com.proyuts.app.repository.EstadoCursoRepository;
import com.proyuts.app.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/cursos")
public class CursoWebController {

    private final CursoService cursoService;
    private final CategoriaRepository categoriaRepository;
    private final EstadoCursoRepository estadoCursoRepository;

    public CursoWebController(
            CursoService cursoService,
            CategoriaRepository categoriaRepository,
            EstadoCursoRepository estadoCursoRepository) {

        this.cursoService = cursoService;
        this.categoriaRepository = categoriaRepository;
        this.estadoCursoRepository = estadoCursoRepository;
    }

    @GetMapping
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "cursos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("estados", estadoCursoRepository.findAll());
        return "cursos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCurso(Curso curso) {
        cursoService.guardar(curso);
        return "redirect:/web/cursos";
    }

    @GetMapping("/editar/{id}")
    public String editarCurso(@PathVariable Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        model.addAttribute("curso", curso);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("estados", estadoCursoRepository.findAll());
        return "cursos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable Long id) {
        cursoService.eliminar(id);
        return "redirect:/web/cursos";
    }
}
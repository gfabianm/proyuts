package com.proyuts.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo máximo debe ser mayor a 0")
    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @NotNull(message = "Los PROYUTS otorgados son obligatorios")
    @Min(value = 1, message = "Los PROYUTS otorgados deben ser mayor a 0")
    @Column(name = "proyuts_otorgados")
    private Integer proyutsOtorgados;

    @NotNull(message = "Debe seleccionar una categoría")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @NotNull(message = "Debe seleccionar un estado")
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoCurso estado;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Usuario profesor;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Curso() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public Integer getProyutsOtorgados() {
        return proyutsOtorgados;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public EstadoCurso getEstado() {
        return estado;
    }

    public Usuario getProfesor() {
        return profesor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public void setProyutsOtorgados(Integer proyutsOtorgados) {
        this.proyutsOtorgados = proyutsOtorgados;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setEstado(EstadoCurso estado) {
        this.estado = estado;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
package com.proyuts.app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @Column(name = "proyuts_otorgados")
    private Integer proyutsOtorgados;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private EstadoCurso estado;

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

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
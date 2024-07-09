package com.alurachallenge.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="LibrosRegistrados")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idioma;
    private Double numeroDeDescargas;
    private Integer fechaNacimientoAutor;
    private Integer fechaMuerteAutor;

    public Libro() {
    }

    public Libro(String titulo, List<DatosAutor> autores, List<String> idiomas, Double numeroDeDescargas) {
        this.titulo = titulo;
        this.autor = autores != null && !autores.isEmpty() ? autores.get(0).nombre() : null;
        this.idioma = idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : null;
        this.numeroDeDescargas = numeroDeDescargas != null ? numeroDeDescargas : 0.0;
        this.fechaNacimientoAutor = autores != null && !autores.isEmpty() ? autores.get(0).fechaDeNacimiento() : null;
        this.fechaMuerteAutor = autores != null && !autores.isEmpty() ? autores.get(0).fechaDeMuerte() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Integer getFechaNacimientoAutor() {
        return fechaNacimientoAutor;
    }

    public void setFechaNacimientoAutor(Integer fechaNacimientoAutor) {
        this.fechaNacimientoAutor = fechaNacimientoAutor;
    }

    public Integer getFechaMuerteAutor() {
        return fechaMuerteAutor;
    }

    public void setFechaMuerteAutor(Integer fechaMuerteAutor) {
        this.fechaMuerteAutor = fechaMuerteAutor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", idioma='" + idioma + '\'' +
                ", numeroDeDescargas=" + numeroDeDescargas +
                ", fechaNacimientoAutor=" + fechaNacimientoAutor +
                ", fechaMuerteAutor=" + fechaMuerteAutor +
                '}';
    }
}

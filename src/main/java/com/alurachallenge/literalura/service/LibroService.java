package com.alurachallenge.literalura.service;

import com.alurachallenge.literalura.model.Libro;
import com.alurachallenge.literalura.model.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional
    public void guardarLibro(Libro libro) {
        libroRepository.save(libro);
        System.out.println("Libro guardado en la base de datos");
    }
}
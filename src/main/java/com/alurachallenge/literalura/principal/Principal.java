package com.alurachallenge.literalura.principal;

import com.alurachallenge.literalura.model.Datos;
import com.alurachallenge.literalura.model.DatosLibros;
import com.alurachallenge.literalura.model.Libro;
import com.alurachallenge.literalura.model.repository.LibroRepository;
import com.alurachallenge.literalura.service.ConsumoAPI;
import com.alurachallenge.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private String json;
    private LibroRepository repositorio;

    @Autowired
    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        int opcion;
        do {
            mostrarOpciones();
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    verLibrosGuardados();
                    break;
                case 3:
                    verAutoresGuardados();
                    break;
                case 4:
                    buscarAutoresPorAno();
                    break;
                case 5:
                    buscarLibrosPorIdioma();
                    break;
                case 6:
                    mostrarTop10LibrosMasDescargados();
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 7);
        teclado.close();
    }

    private void mostrarOpciones() {
        System.out.println("----- Menú Principal -----");
        System.out.println("1. Buscar libro por nombre");
        System.out.println("2. Ver lista de libros guardados");
        System.out.println("3. Ver lista de autores guardados");
        System.out.println("4. Buscar autores por año");
        System.out.println("5. Buscar libros por idioma");
        System.out.println("6. Mostrar top 10 libros más descargados");
        System.out.println("7. Salir del programa");
        System.out.print("Seleccione una opción: ");
    }

    private void buscarLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        String tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            DatosLibros libroBuscado = datosBusqueda.resultados().get(0);

            // Imprimir los datos obtenidos para verificar
            System.out.println("Datos del libro obtenido:");
            System.out.println("Titulo: " + libroBuscado.titulo());
            System.out.println("Autores: " + libroBuscado.autor());
            System.out.println("Idiomas: " + libroBuscado.idiomas());
            System.out.println("Número de Descargas: " + libroBuscado.numeroDeDescargas());

            Libro libro = new Libro(libroBuscado.titulo(), libroBuscado.autor(), libroBuscado.idiomas(), libroBuscado.numeroDeDescargas());
            repositorio.save(libro);
            System.out.println("Libro guardado en la base de datos");
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void verLibrosGuardados() {
        List<Libro> librosGuardados = repositorio.findAll();
        if (librosGuardados.isEmpty()) {
            System.out.println("No hay libros guardados en la base de datos.");
        } else {
            System.out.println("Lista de libros guardados:");
            for (Libro libro : librosGuardados) {
                System.out.println(libro);
            }
        }
    }

    private void verAutoresGuardados() {
        List<Libro> librosGuardados = repositorio.findAll();
        if (librosGuardados.isEmpty()) {
            System.out.println("No hay autores guardados en la base de datos.");
        } else {
            System.out.println("Lista de autores guardados:");
            for (Libro libro : librosGuardados) {
                System.out.println("Autor: " + libro.getAutor() +
                        ", Fecha de Nacimiento: " + libro.getFechaNacimientoAutor() +
                        ", Fecha de Muerte: " + libro.getFechaMuerteAutor());
            }
        }
    }

    private void buscarAutoresPorAno() {
        System.out.println("Ingrese el año para buscar autores que estaban vivos en ese intervalo de tiempo:");
        int ano = teclado.nextInt();
        teclado.nextLine();  // Limpiar el buffer del teclado

        List<Libro> librosGuardados = repositorio.findAll();
        boolean autorEncontrado = false;

        for (Libro libro : librosGuardados) {
            Integer fechaNacimiento = libro.getFechaNacimientoAutor();
            Integer fechaMuerte = libro.getFechaMuerteAutor();

            if (fechaNacimiento != null && fechaMuerte != null && fechaNacimiento <= ano && fechaMuerte >= ano) {
                System.out.println("Autor: " + libro.getAutor() +
                        ", Fecha de Nacimiento: " + fechaNacimiento +
                        ", Fecha de Muerte: " + fechaMuerte);
                System.out.println("Libros del autor:");
                for (Libro libroAutor : librosGuardados) {
                    if (libroAutor.getAutor().equals(libro.getAutor())) {
                        System.out.println("  - " + libroAutor.getTitulo());
                    }
                }
                autorEncontrado = true;
            }
        }

        if (!autorEncontrado) {
            System.out.println("No se encontraron autores que estuvieran vivos en el año " + ano);
        }
    }

    private void buscarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para buscar libros:");
        String idioma = teclado.nextLine();

        // Convertir el idioma a su abreviatura correspondiente
        String abreviaturaIdioma = convertirIdiomaAAbreviatura(idioma);

        List<Libro> librosGuardados = repositorio.findAll();
        boolean libroEncontrado = false;

        System.out.println("Lista de libros en el idioma " + idioma + ":");
        for (Libro libro : librosGuardados) {
            if (libro.getIdioma().equalsIgnoreCase(abreviaturaIdioma)) {
                System.out.println("Titulo: " + libro.getTitulo() +
                        ", Autor: " + libro.getAutor() +
                        ", Idioma: " + libro.getIdioma() +
                        ", Número de Descargas: " + libro.getNumeroDeDescargas());
                libroEncontrado = true;
            }
        }

        if (!libroEncontrado) {
            System.out.println("No se encontraron libros en el idioma " + idioma);
        }
    }

    private String convertirIdiomaAAbreviatura(String idioma) {
        Map<String, String> mapaIdiomas = new HashMap<>();
        mapaIdiomas.put("español", "es");
        mapaIdiomas.put("ingles", "en");
        mapaIdiomas.put("francés", "fr");
        mapaIdiomas.put("alemán", "de");
        // Agregar más idiomas según sea necesario

        return mapaIdiomas.getOrDefault(idioma.toLowerCase(), idioma);
    }
    private void mostrarTop10LibrosMasDescargados() {
        System.out.println("Mostrando el top 10 de libros más descargados:");
        json = consumoAPI.obtenerDatos(URL_BASE + "?sort=download_count&languages=en");
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda != null && datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            List<DatosLibros> top10Libros = datosBusqueda.resultados().stream()
                    .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            for (DatosLibros libro : top10Libros) {
                System.out.println("Titulo: " + libro.titulo() +
                        ", Autores: " + libro.autor() +
                        ", Idiomas: " + libro.idiomas() +
                        ", Número de Descargas: " + libro.numeroDeDescargas());
            }
        } else {
            System.out.println("No se encontraron libros.");
        }
    }
}

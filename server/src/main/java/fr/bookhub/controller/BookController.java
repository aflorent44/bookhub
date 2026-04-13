package fr.bookhub.controller;

import fr.bookhub.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final GenreService genreService;

    // Catalogue : Liste de tous les livres
    @GetMapping("/api/books")
    public ServiceResponse<?> getCatalog() {
        return bookService.getBooks();
    }

    // Détail d'un livre :
    @GetMapping("/api/books/{id}")
    public ServiceResponse<?> getBookDetails(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    // Ajout d'un livre :
    @PostMapping("/api/books")
    public ServiceResponse addBook(@RequestBody BookCreateRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    // Récupération des genres
    @GetMapping("/api/genres")
    public ServiceResponse<List<GenreDTO>> getGenres() {
        return genreService.getGenres();
    }


}

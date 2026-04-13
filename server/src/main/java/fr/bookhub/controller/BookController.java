package fr.bookhub.controller;

import fr.bookhub.service.*;
import fr.bookhub.service.filter.BookSearchFilter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    // Catalogue : Liste de tous les livres
    @GetMapping("")
    public ServiceResponse<?> getCatalog() {
        return bookService.getBooks();
    }

    // Détail d'un livre :
    @GetMapping("/{id}")
    public ServiceResponse<?> getBookDetails(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    // Ajout d'un livre :
    @PostMapping("")
    public ServiceResponse<?> addBook(@RequestBody @Valid BookCreateRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    // Recherche :
    @PostMapping("/search")
    public ServiceResponse<Page<BookResponse>> search(@RequestBody BookSearchFilter filter) {
        System.out.println(filter);
        return bookService.search(filter);
    }
}

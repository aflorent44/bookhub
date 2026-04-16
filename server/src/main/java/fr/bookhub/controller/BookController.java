package fr.bookhub.controller;

import fr.bookhub.dto.BookCreateRequest;
import fr.bookhub.dto.BookResponse;
import fr.bookhub.service.*;
import fr.bookhub.service.filter.BookSearchFilter;
import fr.bookhub.utility.MethodType;
import fr.bookhub.utility.ServiceResponse;
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
        return bookService.createOrUpdateBook(bookRequest, MethodType.CREATE);
    }

    // Recherche :
    @PostMapping("/search")
    public ServiceResponse<Page<BookResponse>> search(@RequestBody BookSearchFilter filter) {
        return bookService.search(filter);
    }

    // Supprimer un livre :
    @GetMapping("/delete")
    public ServiceResponse<?> deleteBook(@RequestParam int id) {
        return bookService.delete(id);
    }

    // Modifier un livre :
    @PostMapping("/update")
    public ServiceResponse<?> updateBook(@RequestBody BookCreateRequest bookRequest) {
        return bookService.createOrUpdateBook(bookRequest, MethodType.UPDATE);
    }

}

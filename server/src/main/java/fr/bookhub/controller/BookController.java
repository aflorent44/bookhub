package fr.bookhub.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bookhub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.bookhub.entity.Book;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public String getJSONBooksList() throws JsonProcessingException {
        System.out.println("getJSONBooksList");
        List<Book> books = bookService.getBooks();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(books);
    }
}

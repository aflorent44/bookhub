package fr.bookhub.service;

import fr.bookhub.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import fr.bookhub.entity.Book;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}
